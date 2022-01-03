do_install:append() {
    # we don't need this for rfid-jukebox, file will be installed in rfid-jukebox3 recipe
    rm -rf ${D}/${sysconfdir}/mpd.conf
}