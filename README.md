# Documentație - Chat application
## Mihai Rareș Daniel

---

## Specificația sistemului
Aplicația este o platformă web de socializare cu mesagerie instantă.

Utilizatorii pot să își facă un profil și să comunice folosind grupuri sau mesaje directe. Orice utilizator poate să creeze, să editeze si să steargă grupurile pe care le administrează, sau sa trimită mesaje altor utilizatori al căror pseudonim le este cunoscut.

Sistemul este organizat sub forma unei arhitecturi client - server. Clienții vor accesa aplicația din browser.

---

## Precizări tehnice
Java este un limbaj de programare popular, independent de platformă, care favorizează modularitatea și reutilizarea. Din aceste motive, Java a fost alegerea mea pentru programarea back-endului.

Pentru a reduce timpul necesar dezvoltării și pentru a crește productivitatea, am ales să folosesc Spring Boot.

Interfața grafică este realizată în HTML/CSS și Angular.

---

## Arhitectura sistemului
Sistemul este structurat folosind o arhitectură client - server.

Clasele serverului sunt organizate folosind șablonul arhitectural Layered.

Astfel, se remarcă 3 componente principale: Business, Domain și Persistence.

Baza de date folosită este MySql.

# Tabelele bazei de date

## Tabela "User"

### Descriere
Tabela "User" conține informații despre utilizatorii aplicației.

### Coloane
- `user_id` - identificator unic pentru fiecare utilizator; tipul de date este `int`;
- `username` - numele de utilizator al fiecărui utilizator; tipul de date este `String`;
- `email` - adresa de email a fiecărui utilizator; tipul de date este `String`;
- `password` - parola fiecărui utilizator; tipul de date este `String`.

### Relații
- Mulți la mulți între tabela "User" și tabela "Channel" prin intermediul tabelei "Memberships". Această relație permite unui utilizator să fie membru al mai multor canale și un canal să aibă mai mulți membri.

## Tabela "Channel"

### Descriere
Tabela "Channel" conține informații despre canalele din aplicație.

### Coloane
- `channel_id` - identificator unic pentru fiecare canal; tipul de date este `long`;
- `name` - numele fiecărui canal; tipul de date este `String`.

### Relații
- Mulți la mulți între tabela "Channel" și tabela "User" prin intermediul tabelei "Memberships". Această relație permite unui canal să aibă mai mulți membri și un utilizator să fie membru al mai multor canale.
- Mulți la mulți între tabela "Channel" și tabela "User" prin intermediul tabelei "Admins". Această relație permite unui canal să aibă mai mulți administratori.

## Tabela "Message"

### Descriere
Tabela "Message" conține informații despre mesajele din aplicație.

### Coloane
- `message_id` - identificator unic pentru fiecare mesaj; tipul de date este `long`;
- `text` - textul fiecărui mesaj; tipul de date este `String`;
- `date` - data și ora la care a fost trimis mesajul; tipul de date este `long`.

### Relații
- Unu la mulți între tabela "Message" și tabela "User" prin intermediul coloanei "from". Această relație indică utilizatorul care a trimis mesajul.
- Unu la mulți între tabela "Message" și tabela "Channel" prin intermediul coloanei "to". Această relație indică canalul către care a fost trimis mesajul.

## Tabela "Account"

### Descriere
Tabela "Account" conține informații despre conturile utilizatorilor.

### Coloane
- `id` - identificator unic pentru fiecare cont; tipul de date este `Long`;
- `user_id` - identificatorul unic al utilizatorului asociat acestui cont; tipul de date este `Long`;
- `image` - imaginea de profil a utilizatorului; tipul de date este `byte[]`;
- `name` - numele fișierului de imagine de profil; tip

![diagrama baza de date](pictures/db-diagram.png "Diagrama bazei de date")