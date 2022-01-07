FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

RFID_JUKEBOX_SSID ?= "xxx"    
RFID_JUKEBOX_PSK ?= "xxx"

SRC_URI += "\    
    file://wpa_supplicant@wlan0.service \
"

do_install:append() {
install -D -m0644 ${WORKDIR}/wpa_supplicant@wlan0.service ${D}${systemd_unitdir}/system

install -D -m 0600 /dev/null ${D}/etc/wpa_supplicant.conf
cat <<EOF >${D}/etc/wpa_supplicant.conf
ctrl_interface=DIR=/var/run/wpa_supplicant 
update_config=1
network={
    ssid="${RFID_JUKEBOX_SSID}"
    psk="${RFID_JUKEBOX_PSK}"
    }
EOF

}

SYSTEMD_AUTO_ENABLE= "enable"
SYSTEMD_SERVICE:${PN} = "wpa_supplicant@wlan0.service"

