SRCREV = "648ffc470824c43eb0d16c485f4c24816b32cd6f"

do_deploy:append() {  
    if [ -n "${WM8960_AUDIO_HAT}" ]; then
    	sed -i -e 's:#dtparam=i2c_arm=off:dtparam=i2c_arm=on:g'  ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
    
     	echo "dtoverlay=i2s-mmap" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
     	echo "dtparam=i2s=on" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
     	echo "dtoverlay=wm8960-soundcard" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
    fi
}
