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
				  python3-spidev \
				  mopidy \
				  grep \
				  at \
				  mpc \
				  mpg123 \
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
    			  ffmpeg \
				  sudo "
# 				  python3-gpiozero 
				  
# RDEPENDS:${PN} = "python3 python3-dev python3-pip python3-mutagen python3-gpiozero python3-spidev mopidy mopidy-mpd mopidy-local mopidy-spotify samba samba-common-bin gcc lighttpd php7.3-common php7.3-cgi php7.3 at mpd mpc mpg123 git ffmpeg resolvconf spi-tools netcat alsa-tools libspotify12 python3-cffi python3-ply python3-pycparser python3-spotify" 

SRC_URI = "git://github.com/MiczFlor/RPi-Jukebox-RFID.git;protocol=https;branch=future3/develop \
		   file://rfidjukebox.sudoers" 

S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"
PV = "dev+git${SRCPV}"

inherit useradd systemd

SYSTEMD_SERVICE:${PN} = "jukebox-daemon.service"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-u 1111 -d /home/pi --groups www-data --user-group pi "

GROUPADD_PARAM:${PN} = "-r www-data"


user_group = "www-data"
mod = "777"

GIT_REPO_NAME="RPi-Jukebox-RFID"
HOME_PATH="/home/pi"
INSTALLATION_PATH="${HOME_PATH}/${GIT_REPO_NAME}"
SHARED_PATH="${INSTALLATION_PATH}/shared"
SETTINGS_PATH="${SHARED_PATH}/settings"


do_compile () {
  echo "  Building web application"
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
	
    chown -R pi:www-data ${D}${INSTALLATION_PATH}

    echo "  Register Jukebox settings"
    cp ${S}/resources/default-settings/jukebox.default.yaml ${D}${SETTINGS_PATH}/jukebox.yaml
    cp ${S}/resources/default-settings/logger.default.yaml ${D}${SETTINGS_PATH}/logger.yaml    
    
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
}

FILES:${PN} = "/*"

INSANE_SKIP:${PN} += "file-rdeps"

# use local source instead of github
#inherit externalsrc
#EXTERNALSRC = "/home/xxx/git/RPi-Jukebox-RFID"
#EXTERNALSRC_BUILD = "/home/xxx/git/RPi-Jukebox-RFID"

