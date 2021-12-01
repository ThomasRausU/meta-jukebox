FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\    
    file://wireless.network \
"

do_install() {
	install -D -m0644 ${WORKDIR}/wireless.network ${D}${systemd_unitdir}/network/90-wireless.network

}
