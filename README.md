# 🔑 Locks and Keys — Cerca en Espai d’Estats

**Repositori:**  
👉 [https://github.com/nil-romero-upc/LocksAndKeys](https://github.com/nil-romero-upc/LocksAndKeys)

---

## 📘 Descripció del projecte

Aquesta pràctica forma part de l’assignatura **PROP — Projecte de Programació**.  
El projecte consisteix en implementar diversos **algorismes de cerca** sobre un entorn tipus “laberint” on un o més agents han de recollir claus i arribar a la sortida, respectant les restriccions de moviment i obertura de portes.

El mapa defineix:
- **Agents** (posicions inicials numerades)
- **Claus (`a`–`z`)** i **portes (`A`–`Z`)**
- **Parets (`#`)**, **espais buits (`·`)** i **sortida (`@`)**

L’objectiu és trobar un **camí vàlid i òptim** des de l’estat inicial fins a la meta, considerant les regles del joc i el cost acumulat dels moviments.

---

## 🎯 Objectius principals

- Implementar els principals **algorismes de cerca informada i no informada**:
  - **BFS** (Breadth-First Search)
  - **DFS** (Depth-First Search)
  - **IDS** (Iterative Deepening Search)
  - **A\*** (A-Star Search)

- Implementar dos tipus de **control de cicles**:
  - *Branca local* (`usarLNT = false`)
  - *Global amb Llista de Nodes Tancats (LNT)* (`usarLNT = true`)

- Desenvolupar dues **heurístiques** per a l’algorisme A\*:
  - `HeuristicaBasica`: basada en la distància Manhattan fins a la clau pendent o sortida més propera.
  - `HeuristicaAvancada`: combinació optimitzada que minimitza la distància entre agents, claus pendents i sortida, amb ús de cache de distàncies per reduir temps de càlcul.

- Avaluar els algorismes sobre diversos mapes (`mapA`, `mapB`, `mapC`, `mapD`) i mesurar:
  - Nodes explorats i tallats
  - Mida màxima de memòria
  - Temps d’execució
  - Longitud del camí trobat

---

## 🧭 Funcionament general

Cada algorisme treballa amb una **Llista de Nodes Oberts (LNO)** i opcionalment una **Llista de Nodes Tancats (LNT)**.  
El comportament varia segons l’estratègia:

| Algorisme | Estructura LNO | Propietat principal |
|------------|----------------|---------------------|
| **BFS** | `Queue` (FIFO) | Troba el camí mínim en nombre de passos |
| **DFS** | `Deque` (LIFO) | Cerca profunda; pot entrar en cicles |
| **IDS** | Repetició DFS amb profunditat creixent | Combina BFS i DFS sense excés de memòria |
| **A\*** | `PriorityQueue` amb `f(n)=g(n)+h(n)` | Cerca informada basada en heurística |

---

## 👥 Membres del projecte

| Nom complet | Universitat |
|--------------|-------------|
| **Erik Millier** | UPC - EPSEVG |
| **Nil Romero** | UPC - EPSEVG |

---

## 👨‍🏫 Professor

**Bernat Orellana**  
Departament de Ciències de la Computació  
Universitat Politècnica de Catalunya (UPC)

---

## 🧩 Estructura del projecte

```bash
src/
├── main/
│ ├── java/edu/epsevg/prop/ac1/
│ │ ├── cerca/ # Algorismes de cerca
│ │ │ ├── Cerca.java
│ │ │ ├── CercaBFS.java
│ │ │ ├── CercaDFS.java
│ │ │ ├── CercaIDS.java
│ │ │ ├── CercaAStar.java
│ │ │ ├── ControlCicles.java
│ │ │ ├── Node.java
│ │ │ └── heuristica/
│ │ │   ├── Heuristica.java
│ │ │   ├── HeuristicaBasica.java
│ │ │   └── HeuristicaAvancada.java
│ │ ├── model/ # Model de mapa, posicions i moviments
│ │ │ ├── Mapa.java
│ │ │ ├── Posicio.java
│ │ │ ├── Direccio.java
│ │ │ └── Moviment.java
│ │ ├── resultat/ # Classe ResultatCerca
│ │ │ └── ResultatCerca.java
│ │ ├── utils/
│ │ │ └── CsvWriter.java
│ │ └── Main.java # Programa principal
│ └── resources/ # Mapas del joc
└──  /test/java/edu/epsevg/prop/ac1/  
  ├── BFSTest.java
  ├── DFSTest.java
  ├── IDSTest.java
  └── MapaTest.java
results.csv # Resultats obtinguts per als diferents Mapes i Cerques
```

---
