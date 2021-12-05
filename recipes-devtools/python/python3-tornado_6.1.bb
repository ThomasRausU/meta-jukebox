inherit pypi setuptools3
require python3-tornado.inc

# Requires _compression which is currently located in misc
RDEPENDS:${PN} += "\
    ${PYTHON_PN}-misc \
    "
