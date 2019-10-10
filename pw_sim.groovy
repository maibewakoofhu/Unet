//! Simulation TDMA

import org.arl.fjage.RealTimePlatform
import org.arl.unet.sim.*
import org.arl.unet.sim.channels.*
import org.arl.unet.phy.*

///////////////////////////////////////////////////////////////////////////////
// channel and modem settings

channel = [
  model:                BasicAcousticChannel,     //by default
  carrierFrequency:     25.kHz,
  bandwidth:            4096.Hz,
  spreading:            2,         //spherical or cylindrical (spreading loss factor)
  temperature:          25.C,
  salinity:             35.ppt,
  noiseLevel:           73.dB,   //PSD of ambient noise
  waterDepth:           1120.m  // 20m on net
]
//channel.model = ProtocolChannelModel


modem.dataRate = [2400, 2400].bps
modem.frameLength = [8, 8].bytes
modem.txDelay = 0


///////////////////////////////////////////////////////////////////////////////
// simulation settings

def nodes = 1..2                    // list of nodes
def T = 1.hours                   // simulation horizon
//trace.warmup = 15.minutes               // collect statistics after a while

println '''
TX Count\tRX Count\tLoss % 
--------\t--------\t------'''
int rx1 = 0
float loss1 = 0
int tx1 = 0

  simulate T ,{
  
  
    node 'C', address: 31, location: [180.m, 0, -1000.m],  stack: { container ->
      container.add 'ping', new pw_class()
  
    }
  
    node 'A', abcd: 10, address: 21, location: [0.m, 0.m, 0.m], stack: { container ->
      container.add 'ping', new pw_class()
    }
    
  }
  //getRxPower(Reception rx)
  float loss = trace.txCount ? 100*trace.dropCount/trace.txCount : 0
  println sprintf('%6d\t\t%6d\t\t%5.1f',
    [trace.txCount, trace.rxCount, loss])
  tx1 =trace.txCount
  rx1 =trace.rxCount
  loss1 =loss

println '''


Average Values
=====================

TX Count\tRX Count\tLoss % 
--------\t--------\t------'''

tx1 = tx1
rx1 = rx1
loss1 = loss1

println sprintf('%6d\t\t%6d\t\t%5.1f',
    [tx1, rx1, loss1])