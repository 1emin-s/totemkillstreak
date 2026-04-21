# 🚀 Hızlı Başlangıç

## 3 Adım - 5 Dakika

### 1️⃣ Ses Dosyalarını Ekle

```
totem-kill-streak/src/main/resources/assets/totemkillstreak/sounds/combo/
```

İçine koy:
- `combo_1.ogg`
- `combo_2.ogg`
- `combo_3.ogg`
- `combo_4.ogg`
- `combo_5.ogg`

### 2️⃣ Build Et

```bash
cd totem-kill-streak
./gradlew build
```

Tamamlandı mı? Kontrol et:
```bash
ls build/libs/totemkillstreak-1.0.0.jar
```

### 3️⃣ Test Et

JAR'ı buraya koy:
```
~/.minecraft/mods/
```

Minecraft 1.21.1 + Fabric aç ve test et:

**Test 1: Kendi Totem**
```
/give @s totem_of_undying
/kill
```
→ Vanilla ses duyulmalı ✅

**Test 2: Başkasının Totem (LAN)**
1. A ve B oyuncu
2. A: `/kill` (totem varsa)
3. B'de combo_1 sesi duyulmalı ✅

---

## ⚡ Sorun Giderme

### Build hatası
```bash
# Cache temizle
./gradlew clean build

# Java 21+ yüklü mü?
java -version
```

### Sesler duyulmuyor
```bash
# Dosyalar mevcut mu?
ls src/main/resources/assets/totemkillstreak/sounds/combo/

# sounds.json doğru mu?
cat src/main/resources/assets/totemkillstreak/sounds.json
```

---

## 📝 Dosya Listesi

```
✅ Tüm Java dosyaları hazır
✅ Gradle config hazır
✅ sounds.json hazır
⬜ Ses dosyaları (5 tane .ogg) ← SEN EKLE
```

---

## 🎯 Ne Yapıyor?

```
Server: "EntityStatusS2CPacket gönder (Status 35 = totem)"
Client: "Bu benim mi?"
  ├─ Evet (kendi) → vanilla ses
  └─ Hayır (başkası) → combo sesi (1,2,3,4,5...)

3 saniye timeout → reset
```

---

**Hepsi bitti! Ses dosyalarını ekle ve build et.** 🎮
