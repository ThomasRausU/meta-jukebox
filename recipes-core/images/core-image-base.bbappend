ENABLE_SPI_BUS="1"

DISTRO_FEATURES_append = "wifi alsa"

IMAGE_FEATURES_remove = "splash"

IMAGE_FEATURES_append = " ssh-server-openssh \
						  package-management"

IMAGE_INSTALL_append = " linux-firmware-bcm43430 \
					     rfid-jukebox \
					     psmisc \
 						 wpa-supplicant \
 						 locale-base-de-de \
 						 alsa-utils \
 						 python3 \
 						 python3-mfrc522 "
 						 
MACHINE_FEATURES_append = "wifi"


IMAGE_CLASSES += "sdcard_image-rpi"


