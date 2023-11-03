# Truco_Java

![Logo](Truco_Java/src/truco_java/fondos/logo.png)

## Índice
<!-- vim-markdown-toc GFM -->

* [¿Qué es?](#qué-es)
    * [¿El modo Multijugador es por LAN o por Internet?](#el-modo-multijugador-es-por-lan-o-por-internet)
    * [¿Cómo se juega el Truco argentino?](#cómo-se-juega-el-truco-argentino)
* [Dependencias](#dependencias)
* [Instalación - ¿Cómo puedo jugar?](#instalación---cómo-puedo-jugar)
    * [Compilación](#compilación)
* [Screenshots](#screenshots)
* [¿Por qué este juego no tiene flor?](#por-qué-este-juego-no-tiene-flor)
* [¿Qué hago si no funciona el audio y no funciona correctamente el juego? :-(](#qué-hago-si-no-funciona-el-audio-y-no-funciona-correctamente-el-juego--)
* [¿Qué hago si en Windows se abre como un archivo comprimido? :-(](#qué-hago-si-en-windows-se-abre-como-un-archivo-comprimido--)
* [Disclaimer](#disclaimer)
* [Agradecimientos](#agradecimientos)

<!-- vim-markdown-toc -->

## ¿Qué es?
Esta aplicación es un [juego de *truco argentino*](https://es.wikipedia.org/wiki/Truco_argentino) hecho en *java* (mediante [ant](https://en.wikipedia.org/wiki/Apache_Ant),[Neovim](http://neovim.io/) y [NetBeans](https://es.wikipedia.org/wiki/NetBeans)). En este juego podrás jugar con un amigo por red o una partida rápida, con una inteligencia que tiene dos modos:
* Normal: En donde la "Inteligencia" (algoritmo) puede mentir, y jugar de manera más astuta/inteligente.
* Fácil: Ideado para principiante que comienzan en el truco. Las partidas que se ganen o pierdan, no serán contadas en el puntaje de partidas ganadas.

Ah, por cierto, esta versión [no contiene la opción de cantar 'flor'](https://github.com/Ezee1015/Truco_Java#por-qu%C3%A9-este-juego-no-tiene-flor).

### ¿El modo Multijugador es por LAN o por Internet?
Este juego, además de ofrecer la posibilidad de jugar contra la aplicación (algoritmo), dispone de un modo multijugador en el cual podrás jugar con tus amigos en una partida de 1 vs 1 dentro de la misma red local (dados los permisos correspondientes en el firewall de quien cree la sala de juego). Estos últimos dos modos NO requieren de acceso a internet, aunque, otra opción (con Internet), gracias a la implementación P2P, permite jugar a dos personas en diferentes redes. Esta última opción requerirá de abrir un puerto en el router (que apunte al puerto correspondiente de la PC del servidor) en la red de la persona que cree la sala, para que la computadora cliente pueda comunicarse.

### ¿Cómo se juega el Truco argentino?
[En este link de Wikipedia](https://es.wikipedia.org/wiki/Truco_argentino) hay una interesante guía sobre la historia del truco.

## Dependencias
La única dependencia del juego es una versión de Java reciente y un servidor gráfico (generalmente incluido en los sistemas por defecto). Una dependencia opcional: MPV (en caso que no funcione el sonido a través del reproductor de java).

## Instalación - ¿Cómo puedo jugar?
> Necesitarás tener Java instalado. Personalmente recomiendo [OpenJDK](https://openjdk.org/). Aunque se puede usar [Java Oracle](https://www.java.com/es/)

Puedes descargar un binario según su sistema operativo [desde este link](https://github.com/Ezee1015/Truco_Java/releases).

### Windows
Puede descargar el instalador `Truco_Java_Windows.zip`. Deberá descomprimir el archivo descargado y ejecutar el archivo `INSTALL.bat`. Esto instalará el juego en la carpeta del usuario, y creará un link en el escritorio. A partir de ahí, ejecutando el link en el escritorio, podrá disfrutar del juego.

### Para todos los Sistemas operativos (incluyendo windows)
Sino para **cualquier sistema**, puede descargar la versión portable `Truco_Java_MultiOS.zip`. Esta versión es multisistema, por lo tanto se ejecutara sin problemas en Windows, Linux, etc. Una vez descargado y extraído este archivo comprimido, se puede ejecutar el archivo .jar para iniciar el juego. A partir de ahí, podrá disfrutar del juego de manera portable.

### Android
Si usas un celular **Android**, con [Termux](https://f-droid.org/en/packages/com.termux/) a través de la tienda de [F-Droid](https://f-droid.org/es/) (la cual tiene que ser instalada también).

- Para descargar y ejecutar el script, primero verifique con el siguiente comando que curl está instalado:
```bash
apt install curl
```
- Ahora ejecute esta linea de código que preparará el juego en su celular:
```bash
bash -c "$(curl -fsSL https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/instalarTermux.sh)"
```

- Una vez finalizado, puede cerrar la terminal ingresando...
```bash
exit
```

- Luego de ejecutar el script y reiniciar la sesión en Termux, se debe instalar un visualizador de VNC en el celular (NO en Termux), como por ejemplo [AVNC](https://play.google.com/store/apps/details?id=com.gaurav.avnc&gl=US).

- Dentro de dicho visualizador se debe de agregar una nueva conexión con la siguiente información
    - conexión IP: `127.0.0.1`
    - Puerto: `5901`
    - el usuario se dejará vacío
    - como contraseña, **tendrá que crear una**, y debe ser mayor a 6 dígitos

- La configuración de la interacción con el escritorio remoto (conexión VNC) puede ser configurada desde los ajustes del visualizador de VNC

- Cuando arranque por primera vez el juego (ejecutando el comando `truco` en la pantalla de Termux), se le preguntará **EN TERMUX** la contraseña que creó anteriormente en el visualizador VNC, por lo que deberá ingresarla para poder continuar.

> Deberá ingresar la contraseña únicamente la primera vez que se juega. Luego de eso, quedará almacenada en la configuración y no le volverá a pedirla

### Compilación
> [!WARNING]
> Para poder ejecutar el código fuente se necesitará las siguientes aplicaciones...
>   - `ant`
>   - `java`
>   - Un entorno gráfico (X11 por ejemplo)

Si eres valiente, puedes compilar el código fuente de la siguiente manera:

Puedes clonar el repositorio de git con `git clone https://github.com/Ezee1015/Truco_Java` y una vez dentro de la carpeta se puede ejecutar...
- `make run` que ejecutará la aplicación
- `make jar` que creará un archivo jar con sus recursos (imágenes, audios, etc.) en `/Truco_Java/dist/`
- `make clean` que limpiará la carpeta `/Truco_Java/dist/`

## Screenshots
<h3>Menus</h3>
<div style=" display:flex; flex-direction: row">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/menu.png" alt="Menu" style="width: 380px;;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/menujugar.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/registro.png" alt="Menu" style="width: 380px;;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/login.png" alt="Menu" style="width: 380px;">
<div>
<hr>
<h3>Multijugador</h3>
<div style=" display:flex; flex-direction: row">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/cliente1.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/servidor1.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/cliente2.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/servidor2.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/cliente3.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/servidor3.png" alt="Menu" style="width: 380px;">
<div>
<hr>
<h3>Partida Rápida</h3>
<div style=" display:flex; flex-direction: row">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego1.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego2.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego3.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego4.png" alt="Menu" style="width: 380px;">
<div>

## ¿Por qué este juego no tiene flor?
1. Por un motivo de simplificación del código del juego.
2. Personalmente no juego con flor.

Por lo que por ambas razones anteriores, decidí no incluirlo.

## ¿Qué hago si no funciona el audio y no funciona correctamente el juego? :-(
Si no funciona el audio a través de la solución multimedia que incluye Java por defecto, se puede optar por instalar MPV para hacer funcionar el audio por esa alternativa. En caso que el juego no puede reproducir audio por ninguna de esas vías, se corrompe, por lo tanto, para poder hacer que el juego funcione correctamente, se puede desactivar el sonido desde el menú principal. Con esto el juego no reproducirá ningún sonido, y por ende no se romperá.

## ¿Qué hago si en Windows se abre como un archivo comprimido? :-(
Este es un problema con la configuración por defecto configurada para la apertura de archivos .jar. [Puede solucionarlo siguiendo estas instrucciones](https://stackoverflow.com/questions/57699084/how-to-open-a-jar-file-by-default-on-windows-10-64-bit) o [estas instrucciones](https://stackoverflow.com/questions/394616/running-jar-file-on-windows). El juego se instala en la carpeta del usuario.

## Disclaimer
Muchas gracias por haberse interesado en este juego. No se pretende ni se tiene la intención de ofender, insultar ni discriminar a nadie. Esta aplicación fue diseñada en forma humorística para divertir a sus jugadores. No es nuestra intensión perjudicar a ninguna persona y pedimos disculpas de antemano.

## Agradecimientos
- Gracias a [ReadLoud](https://readloud.net/)  y a [TTSFree](https://ttsfree.com/) por permitir que este juego tuviera sonido de voces cuando la computadora canta truco, envido, etc..
- También agradecer a [Pixabay](pixabay.com) por los efectos de sonido y [la imagen de la cabaña](https://pixabay.com/es/illustrations/cabina-casa-caba%C3%B1a-casa-de-madera-5701374/).
- Las imágenes de la baraja de cartas son de Basquetteur - Trabajo propio, CC BY-SA 3.0, [Ver fuente](https://commons.wikimedia.org/w/index.php?curid=32842304)
