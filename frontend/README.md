# Vue 3 Datatable Przewklocki Itach
## Einführung
In diesem Projekt wurde eine Datenbank mit Hilfe von Vue 3 im Frontend dargestellt. Das Frontend erfüllt zusätzlich die CRUD Funktionen.

## Umsetzung
Für die Tabellendarstellung wurde die Library Vuetify verwendet wudurch die die Datenanzeige als auch die Datenmanipulation einfacher gemacht wurde.

Beispiel für eine Tabelle mit Vuetify:

```jsx
<v-data-table :items="items"></v-data-table><script setup> 
 const items = [    
	 {      
		 name: 'African Elephant',      
		 species: 'Loxodonta africana',      
		 diet: 'Herbivore',      
		 habitat: 'Savanna, Forests',    
	 },    
 ]
</script>
```

### Backend-Anbindung

Die ReST-API aus dem Backend-Beispiel wurde als Basis für die Aufgabe verwendet. Über HTTP-Anfragen (GET, POST, PUT, DELETE) wird der Datenaustausch zwischen Frontend und Backend umgesetzt. Hierbei werden Axios als Schnittstelle eingesetzt.

Beispiel für eine einfache Datenabfrage:

```jsx
import axios from 'axios'

const fetchData = async () => {
  const response = await axios.get('http://localhost:8080/api/tiere')
  items.value = response.data
}

```

### Erweiterbarkeit und Wartbarkeit

Die Implementierung ist modular aufgebaut, um eine leichte Erweiterbarkeit zu ermöglichen. Klassen sind klar voneinander getrennt, API-Aufrufe werden zentral verwaltet. Dadurch bleibt der Code übersichtlich und kann problemlos mit mehr Tabellen erweitert werden.

### Fazit

Durch die Kombination von Vue 3, Vuetify und einer ReST-API kann man eine flexible und moderne Lösung zur Darstellung und Manipulation von Daten in Tabellenform umgesetzt werden. 

### Promptverzeichnis

AI Modell: ChatGPT 3.5

Zeige, wie man mit Vuetify eine einfache Datentabelle in Vue 3 darstellt. Verwende ein statisches Array als Datenquelle

Erkläre, wie man mit Axios eine ReST-API in Vue 3 anbindet. Zeige ein Beispiel, wie Daten vom Backend abgerufen und in einer Vuetify-Tabelle angezeigt werden können

Erstelle Codebeispiele für CRUD-Operationen (Create, Read, Update, Delete) mit Axios in Vue 3
