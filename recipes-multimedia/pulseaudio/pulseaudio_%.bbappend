FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/:"
DESCRIPTION = "this DISABLE automatically load driver modules depending on the \
hardware available, because wm8960 will currently only work when loaded with rate 48000"


SRC_URI += "\
    file://default.pa \
    file://daemon.conf \
    file://wm8960.pa \
    "
    
do_install:append() {
    install -m 0644 ${WORKDIR}/default.pa ${D}/${sysconfdir}/pulse/default.pa

    install -d ${D}/etc/pulse/default.pa.d
    install -m 0644 ${WORKDIR}/wm8960.pa ${D}/etc/pulse/default.pa.d/
    install -m 0644 ${WORKDIR}/daemon.conf ${D}/etc/pulse/
}