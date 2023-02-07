# Truco_Java
![Logo](Truco_Java/src/truco_java/fondos/logo.png)

## ¿Qué es?
Esta aplicación es un [juego de *truco argentino*](https://es.wikipedia.org/wiki/Truco_argentino) hecho en *java* (mediante [ant](https://en.wikipedia.org/wiki/Apache_Ant),[Neovim](http://neovim.io/) y [NetBeans](https://es.wikipedia.org/wiki/NetBeans)). En este juego podrás jugar con un amigo por red local o una partida rápida, con una inteligencia que tiene dos modos:
* Normal: En donde la "Inteligencia" (algoritmo) puede mentir, y jugar de manera más astuta/inteligente.
* Fácil: Ideado para principiante que comienzan en el truco. Las partidas que se ganen o pierdan, no seran contadas en el puntaje de partidas ganadas.

Ah, por cierto, esta versión [no contiene la opción de cantar 'flor'](https://github.com/Ezee1015/Truco_Java#por-qu%C3%A9-este-juego-no-tiene-flor).

## Modo Multijugador por LAN
Este juego, además de ofrecer la posibilidad de jugar contra la aplicación (un algoritmo), dispone de un modo multijugador en el cual podrás jugar con tus amigos en una partida de 1 vs 1 dentro de la misma red local. Ambos modos NO requieren de acceso a internet.

## ¿Cómo se juega el Truco argentino?
[En este link de Wikipedia](https://es.wikipedia.org/wiki/Truco_argentino) hay una interesante guía sobre la historia del truco.

## Dependencias
La única dependencia del juego es una versión de Java reciente y un servidor gráfico (generalmente incluido en los sistemas por defecto). Una dependencia opcional: mpv (en caso que no funcione el sonido a través del reproductor de java).

## ¿Cómo puedo jugar al juego (la aplicación)?
Puedes descargar un binario según su sistema operativo [desde este link](https://github.com/Ezee1015/Truco_Java/releases).
* Si es **Windows**, puede descargar el instalador "Truco_Java_Windows.zip". Deberá descomprimir el archivo descargado y ejecutar el archivo INSTALL.bat. Esto instalará el juego en la carpeta del usuario, y creará un link en el escritorio. A partir de ahí, ejecutando el enlace, podrá disfrutar del juego.
* Sino para **cualquier sistema**, puede descargar la versión portable "Truco_Java_MultiOS.zip". Esta versión es multisistema, por lo tanto se ejecutara sin problemas en Windows, Linux, etc. Una vez descargado y extraído este archivo comprimido, se puede ejecutar el archivo .jar para iniciar el juego. A partir de ahí, podrá disfrutar del juego de manera portable.
* Si usas un celular **Android**, con [Termux](https://play.google.com/store/apps/details?id=com.termux), puedes usar un script para jugar desde el celular con dicha aplicación. Para descargar y ejecutar el script, ejecute la línea de código que se encuentra debajo de este párrafo (deberá tener instalado curl, verifique con el siguiente comando `apt install curl`). Luego de ejecutar el script y reiniciar la sesion en termux, se debe instalar un visualizador de vnc en el celular (NO en Termux), como por ejemplo [AVNC](https://play.google.com/store/apps/details?id=com.gaurav.avnc&gl=US). Así, dentro del visualizador se debe de agregar la siguiente conexion: 127.0.0.1:5901 para poder jugar al truco. La configuración de la interacción con el escritorio remoto (conexión vnc) puede ser configurada desde los ajustes del visualizador de vnc.
```
bash -c "$(curl -fsSL https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/instalarTermux.sh)"
```

## Screenshots
### Menus
<div style=" display:flex; flex-direction: row">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/menu.png" alt="Menu" style="width: 380px;;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/menu jugar.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/registro.png" alt="Menu" style="width: 380px;;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/log in.png" alt="Menu" style="width: 380px;">
<div>
### Multijugador
<div style=" display:flex; flex-direction: row">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/cliente 1.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/servidor 1.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/cliente 2.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/servidor 2.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/cliente 3.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/servidor 3.png" alt="Menu" style="width: 380px;">
<div>
### Partida Rapida
<div style=" display:flex; flex-direction: row">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego1.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego2.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego3.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego4.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego5.png" alt="Menu" style="width: 380px;">
  <img src="https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/screenshots/juego6.png" alt="Menu" style="width: 380px;">
<div>

## ¿Por qué este juego no tiene flor?
1. Por un motivo de simplificación del código del juego.
2. Personalmente no juego con flor.

Por lo que por ambas razones anteriores, dicidí no incluirlo.

## No funciona el audio y no funciona correctamente el juego :-(
Si no funciona el audio a través de la solución multimedia que incluye Java por defecto, se puede optar por instalar mpv para hacer funcionar el audio por esa alternativa. En caso que el juego no puede reproducir audio por ninguna de esas vías, se corrompe, por lo tanto, para poder hacer que el juego funcione correctamente, se puede desactivar el sonido desde el menú principal. Con esto el juego no reproducirá ningún sonido, y por ende no se romperá.

## Disclaimer
Muchas gracias por haberse interesado en este juego. No se pretende ni se tiene la intención de ofender, insultar ni discriminar a nadie. Esta aplicación fue diseñada en forma humorística para divertir a sus jugadores. No es nuestra intensión perjudicar a ninguna persona y pedimos disculpas de antemano.

## Agregadecimientos
Gracias a [ReadLoud](https://readloud.net/)  y a [TTSFree](https://ttsfree.com/) por permitir que este juego tuviera sonido de voces cuando la computadora canta truco, envido, etc..
También agradecer a [Pixabay](pixabay.com) por los efectos de sonido.
