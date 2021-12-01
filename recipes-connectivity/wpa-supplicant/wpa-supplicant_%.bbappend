FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\    
    file://wpa_supplicant@wlan0.service \
"

do_install:append() {
	install -D -m0644 ${WORKDIR}/wpa_supplicant@wlan0.service ${D}${systemd_unitdir}/system

}

SYSTEMD_AUTO_ENABLE= "enable"
SYSTEMD_SERVICE:${PN} = "wpa_supplicant@wlan0.service"

