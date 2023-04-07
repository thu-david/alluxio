/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.dora.client.cli.job;

import alluxio.dora.client.cli.fs.AbstractFileSystemShellTest;
import alluxio.dora.job.SleepJobConfig;
import alluxio.dora.job.util.JobTestUtils;
import alluxio.dora.job.wire.Status;

import org.junit.Test;

public class CancelCommandTest extends JobShellTest {

  @Test
  public void testCancel() throws Exception {
    long jobId = AbstractFileSystemShellTest.sJobMaster.run(new SleepJobConfig(150 * 1000));

    AbstractFileSystemShellTest.sJobShell.run("cancel", Long.toString(jobId));

    JobTestUtils.waitForJobStatus(AbstractFileSystemShellTest.sJobMaster, jobId, Status.CANCELED);
  }
}