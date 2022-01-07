
#this should be set in local.conf
RFID_JUKEBOX_NETWORK_ADDRESS ?= "x.x.x.x/x"
RFID_JUKEBOX_NETWORK_GATEWAY ?= "x.x.x.x"

do_install() {
install -d  -m 0755 ${D}${systemd_unitdir}/network/
install -m 0644 /dev/null ${D}${systemd_unitdir}/network/90-wireless.network
cat <<EOF >${D}${systemd_unitdir}/network/90-wireless.network
[Match]
Name=wlan0

[Network]
DHCP=false
Address=${RFID_JUKEBOX_NETWORK_ADDRESS}
Gateway=${RFID_JUKEBOX_NETWORK_GATEWAY}
EOF

}
