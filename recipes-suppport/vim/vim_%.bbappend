FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://vimrc" 

do_install:append() {   
	install -d ${D}/home/root
    install -m 0644 ${WORKDIR}/vimrc ${D}/home/root/.vimrc
}
FILES:${PN} += "/home/root*"