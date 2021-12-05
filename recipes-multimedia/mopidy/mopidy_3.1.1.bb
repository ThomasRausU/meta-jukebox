SUMMARY = "Music server with MPD and Spotify support"
HOMEPAGE = "https://www.mopidy.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[sha256sum] = "a2d615010cd561bd7c5acf15a7787cf59cbb2856dc99d7ab91a54bbe3fcaebb2"

SRC_URI += "\
    file://mopidy.service \
    file://mopidy.conf \
    file://logging.conf \
    "

PYPI_PACKAGE = "Mopidy"
inherit pypi setuptools3 systemd

UPDATERCPN = "${PN}"

SYSTEMD_SERVICE:${PN} = "mopidy.service"


inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --no-create-home --groups www-data --user-group mopdiy "

do_install:append() {
    install -d ${D}/${ROOT_HOME}/Music
    install -d ${D}/${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/mopidy.service ${D}/${systemd_system_unitdir}
    install -d ${D}/usr/bin
    install -m 0755 ${S}/extra/mopidyctl/mopidyctl ${D}/usr/bin/	
    install -d ${D}${sysconfdir}/mopidy/
    install -m 0644 ${WORKDIR}/mopidy.conf ${D}${sysconfdir}/mopidy/
    install -m 0644 ${WORKDIR}/logging.conf ${D}${sysconfdir}/mopidy/
}

RDEPENDS:${PN} += "\
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-python \
    python3-configargparse \
    python3-pygobject \
    python3-pykka \
    python3-requests \
    python3-tornado \
    python3-xml \
    python3-mopidy-mpd \
    python3-mopidy-local \
    "
#     python-mopidy-iris     python-mopidy-spotify     

FILES:${PN} += " \
    ${ROOT_HOME}/Music \
    ${systemd_system_unitdir}/mopidy.service \
    /usr/bin/* \
    "

SYSTEMD_SERVICE:${PN} = "mopidy.service"
