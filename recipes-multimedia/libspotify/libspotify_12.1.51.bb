DESCRIPTION = "Archive of the latest libspotify releases from Spotify "
HOMEPAGE = "https://github.com/mopidy/libspotify-archive"
LICENSE = "CLOSED"



# https://mopidy.github.io/libspotify-archive/libspotify-12.1.103-Linux-armv6-bcm2708hardfp-release.tar.gz
# SRC_URI[sha256sum] = "d658e6c1978fb46cf33376eb8367a51d024f4014f21beac1dd264532bcc54b24"

# https://github.com/mopidy/libspotify-archive/raw/master/libspotify-12.1.51-Linux-armv6-release.tar.gz
SRC_URI[sha256sum] = "4fb888eeb486578fa3a08e15f5aa2101632e60b56a068553d05d5d4ee0a080cc" 
SRC_URI = "https://github.com/mopidy/libspotify-archive/raw/master/libspotify-12.1.51-Linux-armv6-release.tar.gz \
 		   file://rmldconfig.patch;striplevel=2"
		   
S = "${WORKDIR}/libspotify-12.1.51-Linux-armv6-release"
# S = "${WORKDIR}/libspotify-12.1.103-Linux-armv6-bcm2708hardfp-release"

do_configure () {
}

do_compile () {
}

do_install () {
	prefix=${D} oe_runmake install
}

FILES_${PN} = "/*"

INSANE_SKIP_${PN} += "already-stripped ldflags"