DESCRIPTION = "A Raspberry Pi jukebox, playing local music, podcasts and web radio, streams and spotify triggered by RFID cards and/or web app. All plug and play via USB. GPIO scripts available."
HOMEPAGE = "https://github.com/MiczFlor/RPi-Jukebox-RFID"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=00166340e58faaa5fb0ae892b09bf54f"

DEPENDS = "nodejs-native"

RDEPENDS:${PN} = "bash \
				  nginx \
				  python3 \
				  python3-pip \
				  python3-mutagen \
				  python3-mpd2 \
				  python3-eyed3 \
				  python3-deprecation \
				  python3-spidev \
				  python3-pyalsaaudio \
				  grep \
				  at \
				  mpc \
				  mpg123 \
				  mpd \
				  netcat \
				  alsa-tools \
				  python3-cffi \
				  python3-ply \
				  python3-pycparser \
				  python3-evdev \
 				  python3-pirc522 \
 				  python3-pyserial \
 				  python3-pyzmq \
 				  python3-ruamel-yaml \
 				  python3-packaging \
 				  python3-requests \
 				  python3-filetype \
 				  python3-pulsectl \
 				  pulseaudio-server \
 				  pulseaudio-misc \
 				  alsa-utils-amixer \
			 	  alsa-utils-aplay \
    			ffmpeg \
				  sudo \
          espeak " 

SRC_URI = "git://github.com/MiczFlor/RPi-Jukebox-RFID.git;protocol=https;branch=future3/main \
		   file://jukebox-daemon.service.patch \
		   file://mpd.default.conf.patch" 

S = "${WORKDIR}/git"
#SRCREV = "${AUTOREV}"
#known good version#

SRCREV = "80710235c13504816d0e0d50f9b3506a1e76d224"
PV = "git${SRCPV}"

inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-u 1111 -d /home/pi --groups www-data,audio,spi,input,i2c,gpio --user-group pi"

GROUPADD_PARAM:${PN} = "-r www-data; -r spi; -r input; -r i2c; -r gpio"


user_group = "www-data"
mod = "777"

GIT_REPO_NAME="RPi-Jukebox-RFID"
HOME_PATH="/home/pi"
INSTALLATION_PATH="${HOME_PATH}/${GIT_REPO_NAME}"
SHARED_PATH="${INSTALLATION_PATH}/shared"
SETTINGS_PATH="${SHARED_PATH}/settings"
# Prepare new mpd.conf
AUDIOFOLDERS_PATH="${SHARED_PATH}/audiofolders"
PLAYLISTS_PATH="${SHARED_PATH}/playlists"

#TODO configure card?! wm8960-soundcard
ALSA_MIXER_CONTROL="Speaker"
MPD_CONF_PATH="/home/pi/.config/mpd/"
MPD_CONF_FILE="${MPD_CONF_PATH}mpd.conf"
do_compile[network]="true"

export OPENSSL_MODULES="${STAGING_LIBDIR_NATIVE}/ossl-modules"
export NODE_OPTIONS="--openssl-legacy-provider"

do_compile () {
  echo "Building web application"
  cd ${S}/src/webapp
  npm install
  npm ci --prefer-offline --no-audit --production
  npx browserslist@latest --update-db
  npm run build
}


do_install () {
	install -d  ${D}${INSTALLATION_PATH}

	
	cp -r  ${S}/* ${D}${INSTALLATION_PATH}/
	chmod -R 775 ${D}${INSTALLATION_PATH}/
	
    chown -R pi:pi ${D}${INSTALLATION_PATH}/

    echo "  Register Jukebox settings"
    cp ${S}/resources/default-settings/cards.example.yaml ${D}${SETTINGS_PATH}/cards.yaml
    cp ${S}/resources/default-settings/gpio.example.yaml ${D}${SETTINGS_PATH}/gpio.yaml
    cp ${S}/resources/default-settings/jukebox.default.yaml ${D}${SETTINGS_PATH}/jukebox.yaml
    cp ${S}/resources/default-settings/logger.default.yaml ${D}${SETTINGS_PATH}/logger.yaml    
    
	#TODO 777 is a security risk
	install -m777 -d  ${D}${MPD_CONF_PATH}
	chown -R pi:pi ${D}${MPD_CONF_PATH}
	chown -R pi:pi ${D}${MPD_CONF_PATH}/../
	
    install ${S}/resources/default-settings/mpd.default.conf ${D}${MPD_CONF_FILE}
    chown -R pi:www-data ${D}${MPD_CONF_FILE}
    
    install -d ${D}/home/root/.config/mpd/
    ln -sf /home/pi/.config/mpd/mpd.conf ${D}/home/root/.config/mpd/

    
    sed -i 's|%%JUKEBOX_AUDIOFOLDERS_PATH%%|${AUDIOFOLDERS_PATH}|' ${D}${MPD_CONF_FILE}
    sed -i 's|%%JUKEBOX_PLAYLISTS_PATH%%|${PLAYLISTS_PATH}|' ${D}${MPD_CONF_FILE}
    sed -i 's|%%JUKEBOX_ALSA_MIXER_CONTROL%%|${ALSA_MIXER_CONTROL}|' ${D}${MPD_CONF_FILE}

	install -d ${D}${sysconfdir}/nginx/sites-enabled
	install -d ${D}${sysconfdir}/nginx/sites-available
    install -Dm 0644 ${S}/resources/default-settings/nginx.default ${D}/etc/nginx/sites-available/rfid-jukebox
    ln -s ../sites-available/rfid-jukebox ${D}${sysconfdir}/nginx/sites-enabled/

    
    # delete webappsrc
    rm -rf ${D}${INSTALLATION_PATH}/src/webapp/src
    rm -rf ${D}${INSTALLATION_PATH}/src/webapp/node_modules
    
    # Systemd script  
    install -d ${D}/usr/lib/systemd/user/
    install -m 0644 ${S}/resources/default-services/jukebox-daemon.service ${D}/usr/lib/systemd/user/
    
    # enable as user service
    install -d -o pi ${D}/home/pi/.config/systemd/user/default.target.wants/
    ln -sf /usr/lib/systemd/user/jukebox-daemon.service ${D}/home/pi/.config/systemd/user/default.target.wants/jukebox-daemon.service 
    
    
    # this will enable systemd user mode services to start without a user logged in
    install -d ${D}/var/lib/systemd/linger/
    touch ${D}/var/lib/systemd/linger/pi
    
# otherwise if configured as extsrc the sudoers file must be in local dir as well
install -d  -m 0755 ${D}/etc/sudoers.d/
install -m 0440 /dev/null ${D}/etc/sudoers.d/jukebox
cat <<EOF >${D}/etc/sudoers.d/jukebox
www ALL=(ALL:ALL) NOPASSWD: ALL
pi ALL=(ALL:ALL) NOPASSWD: ALL
EOF
    
# this will allow to access hardware as user
install -d  -m 0755 ${D}/etc/udev/rules.d/
install -m 0644 /dev/null ${D}/etc/udev/rules.d/99-com-gpio.rules
cat <<EOF >${D}/etc/udev/rules.d/99-com-gpio.rules
SUBSYSTEM=="input", GROUP="input", MODE="0660"
SUBSYSTEM=="i2c-dev", GROUP="i2c", MODE="0660"
SUBSYSTEM=="spidev", GROUP="spi", MODE="0660"
SUBSYSTEM=="bcm2835-gpiomem", KERNEL=="gpiomem", GROUP="gpio", MODE="0660"
SUBSYSTEM=="gpio", KERNEL=="gpiochip*", ACTION=="add", PROGRAM="/bin/sh -c 'chown root:gpio /sys/class/gpio/export /sys/class/gpio/unexport ; chmod 220 /sys/class/gpio/export /sys/class/gpio/unexport'"
SUBSYSTEM=="gpio", KERNEL=="gpio*", ACTION=="add", PROGRAM="/bin/sh -c 'chown root:gpio /sys%p/active_low /sys%p/direction /sys%p/edge /sys%p/value ; chmod 660 /sys%p/active_low /sys%p/direction /sys%p/edge /sys%p/value'"

EOF

    chown -R pi:pi ${D}/home/pi
    
    install -d -o pi ${D}/home/pi/.config/systemd/user/default.target.wants/
    ln -sf /usr/lib/systemd/user/mpd.service ${D}/home/pi/.config/systemd/user/default.target.wants/
    

    install -d -o pi ${D}/home/pi/.config/systemd/user/sockets.target.wants/
#    rm ${D}/etc/systemd/system/sockets.target.wants/mpd.socket
    ln -sf /usr/lib/systemd/user/sockets.target.wants/mpd.socket ${D}/home/pi/.config/systemd/user/sockets.target.wants/
    
# this overwrite default settings with setup for rc522 spi rfid reader
cat <<EOF >${D}${SETTINGS_PATH}/rfid.yaml
rfid:
  readers:
    read_00:
      module: rc522_spi
      config:
        spi_bus: 0
        spi_ce: 0
        pin_irq: 24
        pin_rst: 25
        mode_legacy: false
        antenna_gain: 4
        log_all_cards: true
      same_id_delay: 1.0
      log_ignored_cards: false
      place_not_swipe:
        enabled: false
        card_removal_action:
          alias: pause
rfid.pinaction.rpi:
  enabled: false
  pin: 0
  duration: 0.2
  retrigger: true
EOF

#disable gpio controls
cat <<EOF >${D}${SETTINGS_PATH}/gpio.yaml

EOF

}

FILES:${PN} = "/*"

INSANE_SKIP:${PN} += "file-rdeps"

# use local source instead of github
# inherit externalsrc
# EXTERNALSRC = "/home/tro/git/RPi-Jukebox-RFID"
# EXTERNALSRC_BUILD = "/home/tro/git/RPi-Jukebox-RFID"

