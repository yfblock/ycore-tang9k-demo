package ycore

import spinal.core._

// Hardware definition
case class MyTopLevel() extends Component {
  val io = new Bundle {
    val sys_clk = in Bool()
    val sys_rst_n = in Bool()
    val led = out Bits(6 bits)
  }

  noIoPrefix();

  val clk = ClockDomain(
    clock = io.sys_clk,
    reset = io.sys_rst_n,
    config = ClockDomainConfig(resetActiveLevel = LOW)
  )

  val coreArea = new ClockingArea(clk) {
    var counter = Reg(UInt(32 bits)) init 0
    val led_r = Reg(Bits(6 bits)) init B"111110"

    counter := counter + 1

    when(counter > U(0x1fffff)) {
      led_r := (led_r(4 downto 0) ## led_r(5 downto 5))
      counter := 0
    }
    io.led := led_r
  }
}

object MyTopLevelVerilog extends App {
  Config.spinal.generateVerilog(MyTopLevel())
}