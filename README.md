# Fast-Furious

Fast-Furious es un juego de simulación de carreras desarrollado en Java que utiliza **hilos (threads)** para gestionar el progreso de múltiples jugadores en tiempo real. La aplicación cuenta con una interfaz gráfica (GUI) construida con **Swing**.

## Características

- **Simulación Multihilo**: Cada jugador es gestionado por un hilo independiente (`ThreadManage`), permitiendo actualizaciones concurrentes de puntajes y estado.
- **Interfaz Gráfica**: Visualización en tiempo real del progreso de 5 jugadores, incluyendo:
    - Nombre del jugador.
    - Zona horaria y hora actual (formato HH:mm:ss).
    - Puntos acumulados y restantes para la meta.
- **Configurable**: Los parámetros del juego son personalizables a través de un archivo de propiedades.
- **Clasificación**: Muestra una clasificación parcial de los jugadores ordenada por puntaje.

## Estructura del Proyecto

El código fuente se encuentra en la carpeta `src` y sigue el patrón MVC (Modelo-Vista-Controlador) levemente adaptado:

- `src/view`: Contiene la interfaz gráfica (`GameInterface.java`, `Panel.java`).
- `src/model`: Lógica de datos y gestión de hilos (`ThreadManage`, `PlayerResult`, `PointsManage`).
- `src/logic`: Lógica adicional (e.g., `HoraActual.java`).
- `src/resources`: Archivos de configuración y recursos estáticos (`config.properties`, imágenes).

## Configuración

El juego se configura mediante el archivo `src/resources/config.properties`. Puede modificar los siguientes parámetros:

- `namePlayerX`: Nombre del jugador (1-5).
- `zonePlayerX`: Zona horaria del jugador (e.g., `America/Chicago`, `Asia/Tokyo`).
- `numberOfGames`: Número total de partidas a jugar.
- `pointsToWin`: Puntaje meta para ganar.

## Ejecución

1. **Requisitos**: Java Development Kit (JDK) 8 o superior.
2. **Compilación**: Compile los archivos Java ubicados en `src`.
3. **Ejecutar**: La clase principal es `view.GameInterface`.

   Desde la raíz del proyecto (si compila manualmente):
   ```bash
   javac -d bin -sourcepath src src/view/GameInterface.java
   java -cp bin view.GameInterface
   ```
   *Nota: Asegúrese de que la ruta a `src/resources/config.properties` sea accesible desde el directorio de ejecución (el código espera encontrarlo en `src/resources/config.properties`).*

