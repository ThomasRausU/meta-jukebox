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
				  python3-pyalsaaudio \
				  python3-spidev \
				  grep \
				  at \
				  mpc \
				  mpg123 \
				  mpd \
				  git \
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
    			  ffmpeg \
				  sudo " 

SRC_URI = "git://github.com/ThomasRausU/RPi-Jukebox-RFID.git;protocol=https;branch=future3/develop" 

S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"
PV = "dev+git${SRCPV}"

inherit useradd systemd

SYSTEMD_SERVICE:${PN} = "jukebox-daemon.service"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-u 1111 -d /home/pi --groups www-data,audio --user-group pi" 

GROUPADD_PARAM:${PN} = "-r www-data"


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

do_compile () {
  echo "Building web application"
  cd ${S}/src/webapp
  npm ci --prefer-offline --no-audit --production
#TODO necessary?
#  rm -rf build

  npm run build
}


do_install () {
	install -d  ${D}${INSTALLATION_PATH}

	
	cp -r  ${S}/* ${D}${INSTALLATION_PATH}/
	chmod -R 775 ${D}${INSTALLATION_PATH}/
	
    chown -R pi:pi ${D}${INSTALLATION_PATH}

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
    
    install -d  ${D}/home/root/.config/mpd/
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
    install -d ${D}${nonarch_base_libdir}/systemd/system
    install -m 0755 ${S}/resources/default-services/jukebox-daemon.service ${D}${nonarch_base_libdir}/systemd/system
    
# otherwise if configured as extsrc the sudoers file must be in local dir as well
install -d  -m 0755 ${D}/etc/sudoers.d/
install -m 0440 /dev/null ${D}/etc/sudoers.d/jukebox
cat <<EOF >${D}/etc/sudoers.d/jukebox
www ALL=(ALL:ALL) NOPASSWD: ALL
pi ALL=(ALL:ALL) NOPASSWD: ALL
EOF
    
}

FILES:${PN} = "/*"

INSANE_SKIP:${PN} += "file-rdeps"

# use local source instead of github
#inherit externalsrc
#EXTERNALSRC = "/home/xxx/git/RPi-Jukebox-RFID"
#EXTERNALSRC_BUILD = "/home/xxx/git/RPi-Jukebox-RFID"

