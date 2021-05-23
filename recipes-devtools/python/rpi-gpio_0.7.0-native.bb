DESCRIPTION = "A module to control Raspberry Pi GPIO channels"
HOMEPAGE = "https://sourceforge.net/projects/raspberry-gpio-python/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENCE.txt;md5=9b95630a648966b142f1a0dcea001cb7"

SRC_URI = "https://files.pythonhosted.org/packages/cb/88/d3817eb11fc77a8d9a63abeab8fe303266b1e3b85e2952238f0da43fed4e/RPi.GPIO-0.7.0.tar.gz"

inherit  distutils3

S = "${WORKDIR}/RPi.GPIO-0.7.0"

# SRC_URI += "file://0001-Remove-nested-functions.patch"
SRC_URI[md5sum] = "777617f9dea9a1680f9af43db0cf150e"
SRC_URI[sha256sum] = "7424bc6c205466764f30f666c18187a0824077daf20b295c42f08aea2cb87d3f"

# ignore issues with -fno-common from gcc-10 until it's fixed in upstream:
# https://sourceforge.net/p/raspberry-gpio-python/tickets/187/
CFLAGS += "-fcommon"


BBCLASSEXTEND = "native nativesdk"