// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import java.util.ArrayList;

/** Add your docs here. */
public final class UtilRunner {
  private static UtilRunner m_instance;
  private ArrayList<Runnable> m_runnables = new ArrayList<>();

  private UtilRunner() {}

  public static UtilRunner getInstance() {
    if (m_instance == null) {
      m_instance = new UtilRunner();
    }
    return m_instance;
  }

  public void addRunnable(Runnable runnable) {
    m_runnables.add(runnable);
  }

  public void run() {
    for (Runnable runnable : m_runnables) {
      runnable.run();
    }
  }
}
