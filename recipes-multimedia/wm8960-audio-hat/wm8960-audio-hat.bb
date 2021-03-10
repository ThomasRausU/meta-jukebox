DESCRIPTION = "The drivers of [WM8960 Audio HAT] for Raspberry Pi."
HOMEPAGE = "https://github.com/waveshare/WM8960-Audio-HAT"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d32239bcb673463ab874e80d47fae504"

RDEPENDS_${PN} = "alsa-tools"
# SRC_URI = "git://github.com/waveshare/WM8960-Audio-HAT.git"
SRC_URI = "file:///home/tro/git/WM8960-Audio-HAT"

# SRCREV = "${AUTOREV}"
# PV = "master+git${SRCPV}"

# inherit devicetree
inherit module

# S = "${WORKDIR}/git"
S = "${WORKDIR}/home/tro/git/WM8960-Audio-HAT"

# inherit devicetree module


# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

# RPROVIDES_${PN} += "kernel-module-wm8960"

COMPATIBLE_MACHINE = "raspberrypi0-wifi"

WM8960_AUDIO_HAT="1"