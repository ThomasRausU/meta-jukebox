SUMMARY = "A console-only image for raspberry pi zero wh including rfid mfrc522 reader, wm8960 audio hat\
 and rfid jukebox (phoniebox)"

inherit core-image

DISTRO_FEATURES_append = "wifi alsa"

IMAGE_FEATURES_remove = "splash"

IMAGE_FEATURES_append = " ssh-server-openssh \
						  package-management"

IMAGE_INSTALL_append = " base-files \
						 linux-firmware-bcm43430 \
					     rfid-jukebox \
					     psmisc \
 						 wpa-supplicant \
 						 locale-base-de-de \
 						 alsa-utils \
 						 openssh-sftp \
 						 psmisc \
 						 python3 \
 						 python3-mfrc522 \
 						 syslog-ng \
 						 htop \
 						 ntp \
 						 vim "

IMAGE_INSTALL_remove  = "alsa-plugins-pulseaudio-conf"


IMAGE_OVERHEAD_FACTOR = "2"