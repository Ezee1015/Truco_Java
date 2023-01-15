apt update && apt upgrade
apt install xorg-server tigervnc mpv git openjdk-17 ant

cd ~/
git clone https://github.com/Ezee1015/Truco_Java
echo "alias truco='~/truco.sh && exit'" >> ~/.bashrc
echo "alias truco='~/truco.sh && exit'" >> ~/.zshrc

echo '#!/bin/bash' > ~/truco.sh
echo 'cd ~/Truco_Java/Truco_Java' >> ~/truco.sh
echo 'echo "¿Quiére comprobar si hay una nueva version (y descargarla)?"'
echo 'echo "Presione -s- y luego -Enter- para actualizar o solo -Enter- para continuar"' >> ~/truco.sh
echo 'read resp' >> ~/truco.sh
echo 'if [ "$resp" == "s" ]; then' >> ~/truco.sh
echo '  git pull' >> ~/truco.sh
echo 'fi' >> ~/truco.sh
echo 'pkill Xvnc' >> ~/truco.sh
echo 'vncserver -geometry 500x800' >> ~/truco.sh
echo 'export DISPLAY=":1"' >> ~/truco.sh
echo 'ant run' >> ~/truco.sh
echo 'pkill Xvnc' >> ~/truco.sh

chmod +x ~/truco.sh

clear
echo "Ya se ha terminado de instalar. Por favor reinicie la sesión y ejecute 'truco'"
