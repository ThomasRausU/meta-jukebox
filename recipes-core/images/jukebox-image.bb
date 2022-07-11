SUMMARY = "A console-only image for raspberry pi zero wh including rfid mfrc522 reader, wm8960 audio hat\
 and rfid jukebox (phoniebox)"

inherit core-image

IMAGE_FEATURES:remove = "splash"

IMAGE_FEATURES:append = " ssh-server-openssh \
						  package-management \
						  allow-empty-password \
						  empty-root-password"

IMAGE_INSTALL:append = " base-files \
						 linux-firmware-bcm43430 \
						 lsof \
					     psmisc \
 						 wpa-supplicant \
 						 locale-base-de-de \
 						 openssh-sftp-server \
 						 psmisc \
 						 python3 \
 						 tzdata \
 						 htop \
 						 ntp \
 						 vim \
 						 rfid-jukebox3 \
 						 systemd-analyze"


# this will double the image size
#IMAGE_OVERHEAD_FACTOR = "2"

#this will set the image size to 5GB
#IMAGE_ROOTFS_SIZE= "5242880"

# set password toor for root
# you will be forced to change this by the first login
# inherit extrausers
# PASSWD = "$(openssl passwd toor)"
# EXTRA_USERS_PARAMS = "usermod -p ${PASSWD} root;"

PACKAGE_FEED_ARCHS = "all cortexa53 raspberrypi0_2w_64"
# this should be configured in local.conf:
PACKAGE_FEED_URIS ?= "http://x.x.x.x:80"
PACKAGE_FEED_BASE_PATHS ?= "ipk-rpzwh"
