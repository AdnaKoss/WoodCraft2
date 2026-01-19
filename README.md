# 🪵 WoodCraft Planner

**WoodCraft Planner** je desktop aplikacija za planiranje, crtanje i procjenu izrade drvenih elemenata.  
Namijenjena je stolarima, malim radionicama i pojedincima koji se bave obradom drveta.

Aplikacija omogućava vizualno crtanje projekata, automatski proračun materijala i troškova te generisanje PDF izvještaja spremnog za slanje klijentu.

Projekat je razvijen u okviru predmeta **Metode razvoja softvera**, kao simulacija razvoja softverskog rješenja za stvarnog klijenta iz oblasti **WoodCraft industrije**.

---

## 👥 Tim

- **Dino Čolaković** 
- **Iman Gunjević** 
- **Adna Koss**


---

## 🎯 Cilj aplikacije

Cilj aplikacije je olakšati proces planiranja i procjene izrade drvenih proizvoda kroz:

- vizualno crtanje elemenata
- automatsko prepoznavanje zatvorenih oblika
- precizan obračun materijala
- procjenu otpada
- generisanje profesionalne ponude za klijenta

---

## 🧩 Funkcionalnosti

### 🔐 Autentifikacija
- Registracija korisnika
- Prijava (login)
- Uloge korisnika (**ADMIN / USER**)
- Automatsko preusmjeravanje nakon prijave

---

### 📁 Projekti
- Kreiranje novog projekta
- Prikaz projekata korisnika
- Brisanje projekta
- Definisanje dimenzija platna

---

### ✏️ Crtanje
- Dodavanje čvorova
- Spajanje čvorova u ivice
- Automatsko formiranje oblika
- Pomjeranje čvorova
- Brisanje čvorova
- Selekcija oblika

---

### 📐 Kote i vodilice
- Dodavanje kota
- Pomjeranje kota
- Brisanje kota
- Horizontalne i vertikalne vodilice
- Snapovanje elemenata

---

### 🪚 Rezanje oblika
- Definisanje linije reza
- Dijeljenje postojećih oblika
- Automatsko kreiranje novih podoblika

---

### 🧱 Materijali
- Dodavanje i uređivanje materijala
- Tipovi:
  - Ploča (Sheet)
  - Građa (Lumber)
- Cijena po ploči, m² ili dužnom metru
- Smjer godova
- Slika materijala
- Zadani materijal za projekte

---

### 📊 Proračuni
- Površina oblika
- Obim oblika
- Lista rezova
- Procjena otpada
- Ukupna cijena projekta

---

### 📄 PDF export
- Generisanje PDF izvještaja
- Pregled projekta
- Materijali
- Rezovi
- Ukupni troškovi
- Spremno za klijenta

---

### 🔄 Undo / Redo
- Vraćanje prethodnih koraka
- Historija izmjena

---

### 🔍 Ostalo
- Zoom in / zoom out
- Promjena mjernih jedinica (cm / in)
- Administratorski meni
- Upravljanje korisnicima

---

## 🛠️ Tehnologije

- Java 21  
- JavaFX  
- Maven  
- SQLite  
- MVC arhitektura  
- DAO pattern  
- Git & GitHub  

---

## 🧠 Arhitektura

Aplikacija je podijeljena na:

- **UI sloj** – JavaFX Views  
- **Controller sloj** – upravljanje događajima  
- **Service sloj** – poslovna logika  
- **DAO sloj** – rad sa bazom podataka  
- **Model sloj** – entiteti  

---

## ▶️ Pokretanje aplikacije

1. Klonirati repozitorij i Otvoriti projekat u IntelliJ IDEA ili VS Code:

Pokrenuti:

```bash
git clone https://github.com/DinoColakovic/WoodCraft2

AppLauncher.java
