# FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

do_install_append() {   
	install -d ${D}/home/root
    install -m 0644 vimrc ${D}/home/root/.vimrc
}
