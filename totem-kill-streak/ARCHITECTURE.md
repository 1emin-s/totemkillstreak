# 🏗️ Yeni Mimarı - Detaylı

## 📊 Packet-Based Architecture

```
┌─────────────────────────────────────┐
│   Minecraft Server                   │
│  (Oyuncu totem kullanıyor)          │
└──────────────┬──────────────────────┘
               │
        ┌──────▼──────────────────┐
        │ EntityStatusS2CPacket   │
        │ Status = 35 (Totem)     │
        │ Entity = PlayerEntity    │
        └──────┬───────────────────┘
               │
        ┌──────▼──────────────────┐
        │   Client (Sende)         │
        │ PacketHandler register   │
        └──────┬───────────────────┘
               │
        ┌──────▼──────────────────┐
        │ onEntityStatus()         │
        │ Status == 35?            │
        └──────┬───────────────────┘
               │
        ┌──────▼──────────────────┐
        │ Entity == client.player? │
        └──┬────────────────────┬──┘
           │                    │
      YES  │                    │  NO
       ┌───▼───┐            ┌──▼────┐
       │ Return │            │ Combo! │
       │ (kendi)│            └──┬─────┘
       └───────┘               │
                        ┌──────▼────────┐
                        │ComboTracker    │
                        │.onTotem()      │
                        └──┬─────────────┘
                           │
                        ┌──▼────────────┐
                        │AudioManager    │
                        │.playComboSound │
                        └────────────────┘
```

---

## 🔍 EntityStatusS2CPacket - Status Kodları

Minecraft'ta EntityStatusS2CPacket'in bazı status kodları:

```java
5  = LivingEntity damage/hurt sound
15 = Armor stand broken
35 = Totem of Undying activation ← BİZ
```

Status 35 = Totem activation!

---

## 💻 PacketHandler.java - Detaylı

### Method Signature

```java
ClientPlayNetworking.registerGlobalReceiver(
    EntityStatusS2CPacket.TYPE,  // Paket tipi
    (packet, context) -> onEntityStatus(packet)  // Listener
);
```

### onEntityStatus Flow

```java
public static void onEntityStatus(EntityStatusS2CPacket packet) {
    // 1️⃣ Status kontrol
    if (packet.getStatus() != TOTEM_STATUS) {
        return;  // Totem değil, skip
    }
    
    // 2️⃣ World/Player null check
    MinecraftClient client = MinecraftClient.getInstance();
    if (client.world == null || client.player == null) {
        return;  // World yüklenmemiş
    }
    
    // 3️⃣ Entity çözümle (network ID → Entity instance)
    Entity entity = packet.getEntity(client.world);
    if (entity == null) {
        return;  // Entity bulunamadı
    }
    
    // 4️⃣ Kendi mi kontrol et
    if (entity == client.player) {
        return;  // Kendi totemim, vanilla ses zaten çalıyor
    }
    
    // 5️⃣ Başkasının → Combo başlat
    ComboTracker.INSTANCE.onTotem();
    AudioManager.playComboSound(ComboTracker.INSTANCE.getCurrentLevel());
}
```

---

## 🎯 Neden `entity == client.player`?

Java instance comparison:

```java
// Yanlış (String gibi)
if (entity.getName().equals(client.player.getName())) { ... }

// Doğru (entity instance)
if (entity == client.player) { ... }
```

`==` operator **aynı memory address'i** kontrol eder:
- `client.player` = Local player (kendimiz)
- Paket'teki entity = Patladığı entity

Eğer ikisi aynı memory address'ise = kendi totemimiz.

---

## 🔔 Singleton Pattern - ComboTracker

```java
public class ComboTracker {
    public static final ComboTracker INSTANCE = new ComboTracker();
    
    private ComboTracker() { }  // Constructor private
}
```

**Neden singleton?**
- Sadece bir combo state olacak
- Tüm koddan aynı instance'a erişim
- `ComboTracker.INSTANCE.onTotem()` basit

**Kullanım:**
```java
ComboTracker.INSTANCE.onTotem();
int level = ComboTracker.INSTANCE.getCurrentLevel();
```

---

## ⏱️ Timeout Mekanizmi

```java
public void onTotem() {
    long now = System.currentTimeMillis();
    long timeSinceLastCombo = now - lastComboTime;
    
    if (timeSinceLastCombo > COMBO_TIMEOUT_MS) {
        // 3 saniye geçti → reset
        currentLevel = 1;
    } else {
        // Timeout içinde → level artır
        if (currentLevel < MAX_LEVEL) {
            currentLevel++;
        }
    }
    
    lastComboTime = now;  // Zamanı güncelle
}
```

**Örnek Timeline:**

```
T=0s:   onTotem() → level = 1, lastTime = 0
T=1s:   onTotem() → level = 2, lastTime = 1000
T=2s:   onTotem() → level = 3, lastTime = 2000
T=3s:   (hiç çağırılmadı)
T=4s:   onTotem() → (4000 - 2000 = 2000ms > 3000ms?)
        → Hayır! (2000 < 3000)
        → level = 4, lastTime = 4000
T=5.5s: onTotem() → (5500 - 4000 = 1500ms > 3000ms?)
        → Hayır
        → level = 5 (max), lastTime = 5500
T=8.6s: onTotem() → (8600 - 5500 = 3100ms > 3000ms?)
        → Evet! Timeout!
        → level = 1 (RESET), lastTime = 8600
```

---

## 🔊 AudioManager - Basit

```java
public static void playComboSound(int level) {
    // Level 5+ için combo_5 kullan
    int soundIndex = Math.min(level - 1, 4);
    
    // SoundEvent oluştur ve çal
    SoundEvent soundEvent = COMBO_SOUNDS[soundIndex];
    PositionedSoundInstance sound = 
        PositionedSoundInstance.master(soundEvent, 1.0f, 1.0f);
    
    client.getSoundManager().play(sound);
}
```

**Level Mapping:**
```
Level 1 → soundIndex = 0 → COMBO_SOUNDS[0] → combo_1
Level 2 → soundIndex = 1 → COMBO_SOUNDS[1] → combo_2
Level 3 → soundIndex = 2 → COMBO_SOUNDS[2] → combo_3
Level 4 → soundIndex = 3 → COMBO_SOUNDS[3] → combo_4
Level 5+ → soundIndex = 4 → COMBO_SOUNDS[4] → combo_5
```

---

## 🎯 Fabric Event System

Bu mod Fabric API'nin `ClientPlayNetworking` kullanıyor:

```java
// Paket kayıt
ClientPlayNetworking.registerGlobalReceiver(
    EntityStatusS2CPacket.TYPE,
    (packet, context) -> { ... }
);
```

**Event Lifecycle:**
1. Server S2C paket gönder
2. Client Networking'de paket alındı
3. `registerGlobalReceiver` listener çağrılır
4. Handler çalışır

---

## 📊 Senkronizasyon

```
Server (Authority)
├─ Oyuncu A totem kullanıyor
├─ Oyuncu B totem kullanıyor
└─ EntityStatusS2CPacket gönder (her clienta)

Client A                          Client B
├─ Paket al                       ├─ Paket al
├─ Entity check:                  ├─ Entity check:
│  entity = A (kendi)             │  entity = B (başkası)
└─ Skip, vanilla ses              └─ Combo! AudioManager
```

---

## 🚨 Edge Cases

### Case 1: Paket kaybı (rare)
```
Server gönder → Network düştü → Client almadı
→ Combo çalmıyor, tamam (nadiren olur)
```

### Case 2: Hızlı çoklu paket
```
Oyuncu A öldü (paket 1)
Oyuncu B öldü (paket 2) - 10ms sonra
Oyuncu C öldü (paket 3) - 20ms sonra

→ Tüm paketler işlenir
→ Combo 1, 2, 3 çalar
```

### Case 3: Lag/Latency
```
Server'da paket gönder
Client'ta 100ms gecikme alıyor ama yine de işlenir
→ Combo timestamp'i hâlâ doğru
```

---

## ✅ Neden Bu Mimarı Daha İyi?

| Aspect | Sound Event | Packet |
|--------|------------|--------|
| **Authority** | Client (unreliable) | Server (reliable) |
| **Timing** | Hassas (timeout) | Güvenilir (paket ID) |
| **False Positive** | Yüksek | Sıfır |
| **Latency** | +200ms (ses event) | ~10ms (paket) |
| **Code** | Karmaşık | Basit |

---

## 🔮 Gelecek Geliştirmeler

- [ ] HUD combo counter
- [ ] Config file (timeout, volume)
- [ ] Particle effects
- [ ] Sound variations per level
- [ ] Multiplayer leaderboard (server-side)

---

**Packet-based = Daha güvenilir, daha basit.** ✅
