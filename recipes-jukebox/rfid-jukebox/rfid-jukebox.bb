DESCRIPTION = "A Raspberry Pi jukebox, playing local music, podcasts and web radio, streams and spotify triggered by RFID cards and/or web app. All plug and play via USB. GPIO scripts available."
HOMEPAGE = "https://github.com/MiczFlor/RPi-Jukebox-RFID"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=00166340e58faaa5fb0ae892b09bf54f"

RDEPENDS_${PN} = "python3 \
				  python3-pip \
				  python3-mutagen \
				  python3-spidev \
				  mopidy \
				  lighttpd \
				  php \
				  at \
				  mpd \
				  mpc \
				  mpg123 \
				  git \
				  netcat \
				  alsa-tools \
				  python3-cffi \
				  python3-ply \
				  python3-pycparser"
				  
# RDEPENDS_${PN} = "python3 python3-dev python3-pip python3-mutagen python3-gpiozero python3-spidev mopidy mopidy-mpd mopidy-local mopidy-spotify samba samba-common-bin gcc lighttpd php7.3-common php7.3-cgi php7.3 at mpd mpc mpg123 git ffmpeg resolvconf spi-tools netcat alsa-tools libspotify12 python3-cffi python3-ply python3-pycparser python3-spotify" 

SRC_URI = "git://github.com/MiczFlor/RPi-Jukebox-RFID.git"

S = "${WORKDIR}/git"

SRCREV = "${AUTOREV}"
PV = "dev+git${SRCPV}"