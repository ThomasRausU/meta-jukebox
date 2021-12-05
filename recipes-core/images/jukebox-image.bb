SUMMARY = "A console-only image for raspberry pi zero wh including rfid mfrc522 reader, wm8960 audio hat\
 and rfid jukebox (phoniebox)"

inherit core-image


DISTRO_FEATURES:append = " wifi alsa pulseaudio"

IMAGE_FEATURES:remove = "splash"

IMAGE_FEATURES:append = " ssh-server-openssh \
						  package-management"

IMAGE_INSTALL:append = " base-files \
						 linux-firmware-bcm43430 \
						 lsof \
					     psmisc \
 						 wpa-supplicant \
 						 locale-base-de-de \
 						 alsa-utils \
 						 openssh-sftp-server \
 						 psmisc \
 						 python3 \
 						 syslog-ng \
    			 		 tzdata \
 						 htop \
 						 ntp \
 						 vim \
 						 rfid-jukebox"

IMAGE_INSTALL:remove  = "alsa-plugins-pulseaudio-conf"


IMAGE_OVERHEAD_FACTOR = "2"


# set password toor for root
# you will be forced to change this by the first login
inherit extrausers
EXTRA_USERS_PARAMS = "usermod -p $(openssl passwd toor) root;"

PACKAGE_FEED_URIS ?= "http://x.x.x.x:80"
PACKAGE_FEED_BASE_PATHS ?= "ipk-rpzwh"
PACKAGE_FEED_ARCHS ?= "all cortexa53 raspberrypi0_2w_64"
