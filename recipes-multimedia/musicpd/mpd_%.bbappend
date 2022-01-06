do_install:append() {
    # we don't need this for rfid-jukebox, file will be installed in rfid-jukebox3 recipe
    rm -rf ${D}/${sysconfdir}/mpd.conf
    
    # modify mpd service and socket system mode to user mode 
    install -d ${D}/usr/lib/systemd/user/
    mv ${D}/lib/systemd/system/mpd.service ${D}/usr/lib/systemd/user/
    
    install -d ${D}/usr/lib/systemd/user/sockets.target.wants/
    mv ${D}/lib/systemd/system/mpd.socket ${D}/usr/lib/systemd/user/sockets.target.wants/        
}

FILES:${PN} += "/*"