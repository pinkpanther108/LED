// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;

/** Add your docs here. */
public class LED extends UtilBase {
  private enum LEDMode {
    CHASE,
    BLINK,
    RAINBOW
  }

  LEDMode m_mode;
  AddressableLED m_led;
  AddressableLEDBuffer m_ledBuffer;
  double m_timestamp;
  double m_iterationTime;

  // Variables for chase
  int m_stripLength;
  int m_voidLength;
  Color m_tailColor;
  Color m_headColor;
  Color m_defaultColor;
  int m_stripPos;

  // Variables for blink
  Color[] m_colors;

  public LED(int port, int length) {
    m_led = new AddressableLED(port);
    m_ledBuffer = new AddressableLEDBuffer(length);
  }

  public void init() {
    m_led.setData(m_ledBuffer);
    m_led.start();
  }

  @Override
  public void run() {
    switch (m_mode) {
      case CHASE:
        chase();
        break;
      case BLINK:
        blink();
        break;
      case RAINBOW:
        rainbow();
        break;
    }
    m_led.setData(m_ledBuffer);
  }
  
  /** Sets the LED to follow the 'chase' pattern */
  public void setModeChase(
      double incrementTime, 
      int stripLength, 
      int voidLength,
      Color tailColor, 
      Color headColor, 
      Color defaultColor) {
    m_mode = LEDMode.CHASE;
    m_iterationTime = incrementTime;
    m_stripLength = stripLength;
    m_voidLength = voidLength;
    m_tailColor = tailColor;
    m_headColor = headColor;
    m_defaultColor = defaultColor;
    m_timestamp = Timer.getFPGATimestamp();
    m_stripPos = 0;
  }

  public void chase() {
    if (Timer.getFPGATimestamp() - m_timestamp > m_iterationTime) {
      m_timestamp += m_iterationTime;
      m_stripPos++;
      m_stripPos %= m_ledBuffer.getLength();
    }
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      int totalLength = m_stripLength + m_voidLength;
      int relIndex = i % totalLength;
      int relPos = m_stripPos % totalLength;
      if (relPos <= relIndex && relIndex < relPos + m_stripLength) {
        // Index is in a strip
        int dx = relIndex - relPos;
        // Slope for red
        double mRed = ((double) (m_headColor.red - m_tailColor.red)) / m_stripLength;
        // Slope for green
        double mGreen = ((double) (m_headColor.green - m_tailColor.green)) / m_stripLength;
        // Slope for blue
        double mBlue = ((double) (m_headColor.blue - m_tailColor.blue)) / m_stripLength;
        m_ledBuffer.setRGB(i, 
            (int) Math.ceil(mRed * dx + m_tailColor.red), 
            (int) Math.ceil(mGreen * dx + m_tailColor.green), 
            (int) Math.ceil(mBlue * dx + m_tailColor.blue));
      } else {
        // Index is not in a strip
        m_ledBuffer.setRGB(i, m_defaultColor.red, m_defaultColor.green, m_defaultColor.blue);
      }
    }
  }

  public void setModeBlink(double iterationTime, Color... colors) {
    m_mode = LEDMode.BLINK;
    m_iterationTime = iterationTime;
    m_colors = colors;
    m_timestamp = Timer.getFPGATimestamp();
  }

  public void blink() {
    if (Timer.getFPGATimestamp() - m_timestamp > m_iterationTime) {
      m_timestamp += m_iterationTime;
    }
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      int totalLength = m_stripLength + m_voidLength;
      int relIndex = i % totalLength;
      int relPos = m_stripPos % totalLength;
  }

  public void rainbow() {

  }
}
