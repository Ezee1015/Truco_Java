# Truco_Java
![Logo](Truco_Java/src/truco_java/fondos/logo.png)

## ¿Qué es?
Esta aplicación es un [juego de *truco argentino*](https://es.wikipedia.org/wiki/Truco_argentino) hecho en *java* (mediante [ant](https://en.wikipedia.org/wiki/Apache_Ant) y [NetBeans](https://es.wikipedia.org/wiki/NetBeans)). Esta versión no contiene la opción de cantar 'flor'.

## ¿Cómo se juega el Truco argentino?
[En este link de Wikipedia](https://es.wikipedia.org/wiki/Truco_argentino) hay una interesante guía sobre la historia del truco.

## Dependencias
La única dependencia del juego es una versión de Java reciente y un servidor gráfico (generalmente incluido en los sistemas por defecto). Opcional: mpv (en caso que no funcione el sonido a traves del reproductor de java).

## ¿Cómo puedo jugar al juego (la aplicación)?
Puedes descargar un binario según su sistema operativo (desde este link)[].
* Si es Windows, puede descargar el instalador "Truco_Java_Windows.zip". Deberá descomprimir el archivo descargado y ejecutar el archivo INSTALL.bat. Esto instalará el juego en la carpeta del usuario, y creará un link en el escritorio. A partir de ahí, ejecutando el enlace, podrá disfrutar del juego.
* Sino para cualquier sistema, puede descargar la versión portable "Truco_Java_MultiOS.zip". Esta versión es multisistema, por lo tanto se ejecutara sin problemas en Windows, Linux, etc. Una vez descargado y extraído este archivo comprimido, se puede ejecutar el archivo .jar para iniciar el juego. A partir de ahí, podrá disfrutar del juego de manera portable.
* Si usas un celular Android compatible con Termux, puedes usar el siguiente script para jugar desde el celular con dicha aplicación. Para descargar y ejecutar el script, ejecute la linea de abajo (deberá tener instalado curl). Luego de ejecutar el script se debe de  instalar un visualizador de vnc en el celular (NO en Termux), como por ejemplo AVNC. Así, dentro del visualizador se debe de agregar la siguiente conexion: 127.0.0.1:5901 para poder jugar al truco. La configuración de la interacción con el escritorio remoto (conexión vnc) puede ser configurada desde los ajustes del visualizador de vnc.
```
bash -c "$(curl -fsSL https://raw.githubusercontent.com/Ezee1015/Truco_Java/main/instalarTermux.sh)"
```

## ¿Por qué este juego no tiene flor?
1. Por un motivo de simplificación del código del juego.
2. Personalmente no juego con flor.

Por lo que por ambas razones anteriores, disidí no incluirlo.

## No funciona el audio y no funciona correctamente el juego :-(
Si no funciona el audio a través de la solución multimedia que incluye Java por defecto, se puede optar por instalar mpv para hacer funcionar el audio por esa alternativa. En caso que el juego no puede reproducir audio por ninguna de esas vías, se corrompe, por lo tanto, para poder hacer que el juego funcione correctamente, se puede desactivar el sonido desde el menú principal. Con esto el juego no reproducirá ningún sonido, y por ende no se romperá.

## Agregadecimientos
Gracias a [ReadLoud](https://readloud.net/)  y a [TTSFree](https://ttsfree.com/) por permitir que este juego tuviera sonido de voces cuando la computadora canta truco, envido, etc..
