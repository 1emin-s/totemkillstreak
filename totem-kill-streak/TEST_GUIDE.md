# 🧪 Test Rehberi

## Hazırlık

1. ✅ Ses dosyaları eklenmiş
2. ✅ `./gradlew build` başarılı
3. ✅ JAR ~/.minecraft/mods/ 'a kopyalanmış
4. ✅ Minecraft 1.21.1 + Fabric başladı

---

## Test 1: Kendi Totem ✅

**Beklenti:** Vanilla ses duyul, combo sesi OLMAMALI

```
/gamemode creative
/give @s totem_of_undying
/kill
```

**Sonuç Kontrol:**
- [ ] Vanilla ses (blip) duyuldu
- [ ] Combo sesi DUYMADI
- [ ] Hata/crash yok

---

## Test 2: Başkasının Totem ✅

**Beklenti:** Combo sesi (combo_1)

**LAN World kurulumu:**
1. World aç → Allow LAN
2. Oyuncu A join
3. Oyuncu B join

**Test:**
```
A: /give @s totem_of_undying
A: /kill
```

**Sonuç:**
- [ ] A'da: vanilla ses (kendi totemi)
- [ ] B'de: combo_1 sesi (başkasının)
- [ ] Hiç crash yok

---

## Test 3: Rapid Combo ✅

**Beklenti:** Hızlı peş peşe combo_1,2,3,4,5

```
/give @s totem_of_undying 10
/kill (combo 1)
/kill (combo 2) - 1 sn içinde
/kill (combo 3) - 1 sn içinde
/kill (combo 4) - 1 sn içinde
/kill (combo 5) - 1 sn içinde
/kill (combo 5) - 1 sn içinde
```

**Sonuç:**
- [ ] Combo_1 duyuldu
- [ ] Combo_2 duyuldu
- [ ] Combo_3 duyuldu
- [ ] Combo_4 duyuldu
- [ ] Combo_5 duyuldu (2x)
- [ ] Sesler çakışıyor (normal)

---

## Test 4: Timeout ✅

**Beklenti:** 3 saniye sonra combo reset

```
/give @s totem_of_undying 3
/kill (combo 1)
/kill (combo 2) - 1 sn sonra
... 3 SANIYE BEKLE ...
/kill (combo 1 RESET!)
```

**Sonuç:**
- [ ] İlk iki totem: combo_1, combo_2
- [ ] 3 saniye sonra: combo_1 (reset)

---

## Test 5: Multiplayer Sync ✅

**Beklenti:** Her oyuncunun kendi combosuna sahip

```
Oyuncu A die → B'de combo sesi
Oyuncu B die → A'da combo sesi
(senkronize olmalı)
```

**Sonuç:**
- [ ] Sesler doğru oyuncudan geldi
- [ ] Timeout independent (her oyuncunun kendi timeout'u)

---

## 📊 Kontrol Listesi

### Ses Dosyaları
```bash
ls src/main/resources/assets/totemkillstreak/sounds/combo/
# combo_1.ogg combo_2.ogg ... combo_5.ogg olmalı
```

### sounds.json
```bash
grep "combo" src/main/resources/assets/totemkillstreak/sounds.json
# 5 adet combo.1, combo.2, ... olmalı
```

### JAR
```bash
ls ~/.minecraft/mods/totemkillstreak-1.0.0.jar
# Var olmalı
```

### Minecraft Logs
```bash
tail ~/.minecraft/logs/latest.log | grep -i "totem\|error\|exception"
# Error olmayabilı
```

---

## 🐛 Sorun Giderme

### Sesler duyulmuyor

**Kontrol:**
1. Ses dosyaları doğru formatta mı? (OGG)
2. Dosya adları eşleşiyor mu? (combo_1.ogg vs sounds.json)
3. Klasör yapısı doğru mu?
   ```
   src/main/resources/assets/totemkillstreak/sounds/combo/
   ```

### Vanilla ses hala duyuluyor (başkasının totem'inde)

**Olmamalı!** Kontrol et:
- Paket düzgün alınıyor mu?
- Entity check başarılı mı?
- Console'da error var mı?

### Crash/Exception

**Console'a bak:**
```
Exception in thread "Render thread"
```

Hata mesajını kopyala ve söyle!

---

## ✅ Başarılı Test Kriterleri

Tüm bunlar TRUE olmalı:

- [x] Build başarılı (JAR oluştu)
- [x] Kendi totem: vanilla ses ✅
- [x] Başkasının totem: combo sesi ✅
- [x] Combo_1,2,3,4,5 sesler var ✅
- [x] 3 saniye timeout çalışıyor ✅
- [x] Hiç crash yok ✅

---

## 📝 Test Raporu

Tüm testler geçtiyse:

```
✅ Totem Kill Streak Mod Başarılı!

- Kendi totem: Vanilla ses
- Başkasının totem: Combo sesi
- Timeout: 3 saniye
- Level: 1-5 + reset
- Crash: Yok
```

---

**Test etmeyi bitir ve feedback ver!** 🎉
