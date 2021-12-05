do_install:append() {
    # we don't need this for rfid-jukebox, will be install there
    rm -rf ${D}/${sysconfdir}/mpd.conf
}