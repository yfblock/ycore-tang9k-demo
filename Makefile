# Genreated by project template

# GOWIN_PREFIX=

ifneq ($(GOWIN_PREFIX),)
GW_SH := $(GOWIN_PREFIX)/IDE/bin/gw_sh
else
GW_SH := gw_sh
endif

SOURCES = src/ycore/*.scala
VERILOG_FILE := build/*.v
BITSTREAM_FILE := impl/pnr/gw.fs

all: $(BITSTREAM_FILE)

$(BITSTREAM_FILE): $(VERILOG_FILE)
	$(GW_SH) src/scripts/pnr.tcl

$(VERILOG_FILE): $(SOURCES) build.sbt src/board/tang-nano-9k.cst
	sbt "runMain ycore.MyTopLevelVerilog"

clean:
	rm -rf impl $(VERILOG_FILE)

flash: $(BITSTREAM_FILE)
	openFPGALoader -b tangnano9k $<

.PHONY: all clan flash