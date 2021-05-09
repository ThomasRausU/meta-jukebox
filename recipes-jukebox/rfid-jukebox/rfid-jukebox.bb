DESCRIPTION = "A Raspberry Pi jukebox, playing local music, podcasts and web radio, streams and spotify triggered by RFID cards and/or web app. All plug and play via USB. GPIO scripts available."
HOMEPAGE = "https://github.com/MiczFlor/RPi-Jukebox-RFID"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=00166340e58faaa5fb0ae892b09bf54f"

# 				  packagegroup-meta-webserver-php 
RDEPENDS_${PN} = "bash \
				  nginx \					
				  python3 \
				  python3-pip \
				  python3-mutagen \
				  python3-spidev \
				  mopidy \				  
				  php-fpm \
				  grep \
				  at \
				  mpc \
				  mpg123 \
				  git \
				  netcat \
				  alsa-tools \
				  python3-cffi \
				  python3-ply \
				  python3-pycparser \
				  python3-evdev \
				  sudo "
				  
# RDEPENDS_${PN} = "python3 python3-dev python3-pip python3-mutagen python3-gpiozero python3-spidev mopidy mopidy-mpd mopidy-local mopidy-spotify samba samba-common-bin gcc lighttpd php7.3-common php7.3-cgi php7.3 at mpd mpc mpg123 git ffmpeg resolvconf spi-tools netcat alsa-tools libspotify12 python3-cffi python3-ply python3-pycparser python3-spotify" 

SRC_URI = "git://github.com/MiczFlor/RPi-Jukebox-RFID.git \
		   file://rfidjukebox.sudoers" 

S = "${WORKDIR}/git"
SRCREV = "305325d5a9c094e4c47efe6f8ec6d5d7d0fd10d1"
# SRCREV = "${AUTOREV}"
PV = "dev+git${SRCPV}"

inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-u 1111 -d /home/pi --groups www-data --user-group pi "

GROUPADD_PARAM_${PN} = "-r www-data"

jukebox_dir = "${D}/home/pi/RPi-Jukebox-RFID"
user_group = "www-data"
mod = "777"

do_compile () {
:
#	bash ./scripts/inc.writeGlobalConfig.sh
}
do_install () {
	install -d  ${D}/var/www/
	install -d  ${jukebox_dir}
	ln -sf /home/pi/RPi-Jukebox-RFID/htdocs ${D}/var/www/RPi-Jukebox-RFID
	
	cp -r  ${S}/* ${jukebox_dir}/
	chmod -R 775 ${jukebox_dir}/
	
    chown -R pi:www-data ${jukebox_dir}
    
    install -d  ${D}/etc/wpa_supplicant/
    ln -sf  ../wpa_supplicant.conf ${D}/etc/wpa_supplicant/wpa_supplicant.conf
    
    # copy shell script for player
    cp "${jukebox_dir}"/settings/rfid_trigger_play.conf.sample "${jukebox_dir}"/settings/rfid_trigger_play.conf

    # creating files containing editable values for configuration
    echo "$AUDIOiFace" > "${jukebox_dir}"/settings/Audio_iFace_Name
    echo "$DIRaudioFolders" > "${jukebox_dir}"/settings/Audio_Folders_Path
    echo "3" > "${jukebox_dir}"/settings/Audio_Volume_Change_Step
    echo "100" > "${jukebox_dir}"/settings/Max_Volume_Limit
    echo "0" > "${jukebox_dir}"/settings/Idle_Time_Before_Shutdown
    echo "RESTART" > "${jukebox_dir}"/settings/Second_Swipe
    echo "${jukebox_dir}/playlists" > "${jukebox_dir}"/settings/Playlists_Folders_Path
    echo "ON" > "${jukebox_dir}"/settings/ShowCover

    # sample file for debugging with all options set to FALSE
    cp "${jukebox_dir}"/settings/debugLogging.conf.sample "${jukebox_dir}"/settings/debugLogging.conf
    chmod 777 "${jukebox_dir}"/settings/debugLogging.conf

    # The new way of making the bash daemon is using the helperscripts
    # creating the shortcuts and script from a CSV file.
    # see scripts/helperscripts/AssignIDs4Shortcuts.php

    # create config file for web app from sample
    cp "${jukebox_dir}"/htdocs/config.php.sample "${jukebox_dir}"/htdocs/config.php
        # make sure bash scripts have the right settings
#    chown pi:www-data "${jukebox_dir}"/scripts/*.sh
    chmod +x "${jukebox_dir}"/scripts/*.sh
#    chown pi:www-data "${jukebox_dir}"/scripts/*.py
    chmod +x "${jukebox_dir}"/scripts/*.py       
    
    
    install -d ${D}/etc/mopidy/
    cp ${jukebox_dir}/misc/sampleconfigs/mopidy-etc.sample ${D}/etc/mopidy/mopidy.conf
    
#        # Change vars to match install config
#        sudo sed -i 's/%spotify_username%/'"$SPOTIuser"'/' "${etc_mopidy_conf}"
#        sudo sed -i 's/%spotify_password%/'"$SPOTIpass"'/' "${etc_mopidy_conf}"
#        sudo sed -i 's/%spotify_client_id%/'"$SPOTIclientid"'/' "${etc_mopidy_conf}"
#        sudo sed -i 's/%spotify_client_secret%/'"$SPOTIclientsecret"'/' "${etc_mopidy_conf}"
#        # for $DIRaudioFolders using | as alternate regex delimiter because of the folder path slash
#        sudo sed -i 's|%DIRaudioFolders%|'"$DIRaudioFolders"'|' "${etc_mopidy_conf}"
#        sed -i 's/%spotify_username%/'"$SPOTIuser"'/' "${mopidy_conf}"
#        sed -i 's/%spotify_password%/'"$SPOTIpass"'/' "${mopidy_conf}"
#        sed -i 's/%spotify_client_id%/'"$SPOTIclientid"'/' "${mopidy_conf}"
#        sed -i 's/%spotify_client_secret%/'"$SPOTIclientsecret"'/' "${mopidy_conf}"
#        # for $DIRaudioFolders using | as alternate regex delimiter because of the folder path slash
#        sudo sed -i 's|%DIRaudioFolders%|'"$DIRaudioFolders"'|' "${mopidy_conf}"
    
    mkdir -p "${jukebox_dir}"/playlists
    chown -R "${user_group}" "${jukebox_dir}"/playlists
    chmod -R "${mod}" "${jukebox_dir}"/playlists

    # make sure the shared folder is accessible by the web server
    chown -R "${user_group}" "${jukebox_dir}"/shared
    chmod -R "${mod}" "${jukebox_dir}"/shared

    # make sure the htdocs folder can be changed by the web server
    chown -R "${user_group}" "${jukebox_dir}"/htdocs
    chmod -R "${mod}" "${jukebox_dir}"/htdocs

    chown -R "${user_group}" "${jukebox_dir}"/settings
    chmod -R "${mod}" "${jukebox_dir}"/settings

    # logs dir accessible by pi and www-data
    chown "${user_group}" "${jukebox_dir}"/logs
    chmod "${mod}" "${jukebox_dir}"/logs

    # audio folders might be somewhere else, so treat them separately
#    chown "${user_group}" "${DIRaudioFolders}"
#    chmod "${mod}" "${DIRaudioFolders}"

    # make sure bash scripts have the right settings
    chown "${user_group}" "${jukebox_dir}"/scripts/*.sh
    chmod +x "${jukebox_dir}"/scripts/*.sh
    chown "${user_group}" "${jukebox_dir}"/scripts/*.py
    chmod +x "${jukebox_dir}"/scripts/*.py
    
    install -d -m 0750 ${D}${sysconfdir}/sudoers.d
    install ../rfidjukebox.sudoers ${D}${sysconfdir}/sudoers.d/rfidjukebox
    
    cp "${jukebox_dir}"/misc/sampleconfigs/startupsound.mp3.sample "${jukebox_dir}"/shared/startupsound.mp3
    cp "${jukebox_dir}"/misc/sampleconfigs/shutdownsound.mp3.sample "${jukebox_dir}"/shared/shutdownsound.mp3

}

FILES_${PN} = "/*"

INSANE_SKIP_${PN} += "file-rdeps"

