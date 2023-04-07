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

package alluxio.dora.master.journal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import alluxio.dora.master.journal.JournalEntryAssociation;
import alluxio.dora.proto.journal.Block.BlockContainerIdGeneratorEntry;
import alluxio.dora.proto.journal.Block.BlockInfoEntry;
import alluxio.dora.proto.journal.Block.DeleteBlockEntry;
import alluxio.dora.proto.journal.File;
import alluxio.dora.proto.journal.File.AddMountPointEntry;
import alluxio.dora.proto.journal.File.AddSyncPointEntry;
import alluxio.dora.proto.journal.File.AsyncPersistRequestEntry;
import alluxio.dora.proto.journal.File.CompleteFileEntry;
import alluxio.dora.proto.journal.File.DeleteFileEntry;
import alluxio.dora.proto.journal.File.DeleteMountPointEntry;
import alluxio.dora.proto.journal.File.InodeDirectoryEntry;
import alluxio.dora.proto.journal.File.InodeDirectoryIdGeneratorEntry;
import alluxio.dora.proto.journal.File.InodeFileEntry;
import alluxio.dora.proto.journal.File.InodeLastModificationTimeEntry;
import alluxio.dora.proto.journal.File.NewBlockEntry;
import alluxio.dora.proto.journal.File.PersistDirectoryEntry;
import alluxio.dora.proto.journal.File.RemoveSyncPointEntry;
import alluxio.dora.proto.journal.File.RenameEntry;
import alluxio.dora.proto.journal.File.SetAclEntry;
import alluxio.dora.proto.journal.File.SetAttributeEntry;
import alluxio.dora.proto.journal.File.UpdateInodeDirectoryEntry;
import alluxio.dora.proto.journal.File.UpdateInodeEntry;
import alluxio.dora.proto.journal.File.UpdateInodeFileEntry;
import alluxio.dora.proto.journal.File.UpdateUfsModeEntry;
import alluxio.dora.proto.journal.Journal.JournalEntry;
import alluxio.dora.proto.journal.Meta.ClusterInfoEntry;
import alluxio.dora.proto.journal.Meta.PathPropertiesEntry;
import alluxio.dora.proto.journal.Meta.RemovePathPropertiesEntry;
import alluxio.dora.proto.journal.Table;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for {@link JournalEntryAssociation}.
 */
public class JournalEntryAssociationTest {

  @Rule
  public ExpectedException mThrown = ExpectedException.none();

  // CHECKSTYLE.OFF: LineLengthExceed
  // This list must contain one of every type of journal entry. If you create a new type of
  // journal entry, make sure to add it here.
  private static List<JournalEntry> ENTRIES = Arrays.asList(
      JournalEntry.newBuilder().setAddMountPoint(AddMountPointEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setAddSyncPoint(AddSyncPointEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setAddTable(Table.AddTableEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setAddTablePartitions(Table.AddTablePartitionsEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setActiveSyncTxId(File.ActiveSyncTxIdEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setAsyncPersistRequest(AsyncPersistRequestEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setAttachDb(Table.AttachDbEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setBlockContainerIdGenerator(BlockContainerIdGeneratorEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setBlockInfo(BlockInfoEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setClusterInfo(ClusterInfoEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setCompleteFile(CompleteFileEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setDeleteBlock(DeleteBlockEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setDeleteFile(DeleteFileEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setDetachDb(Table.DetachDbEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setDeleteMountPoint(DeleteMountPointEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setInodeDirectory(InodeDirectoryEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setInodeDirectoryIdGenerator(InodeDirectoryIdGeneratorEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setInodeFile(InodeFileEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setInodeLastModificationTime(InodeLastModificationTimeEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setNewBlock(NewBlockEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setPathProperties(PathPropertiesEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setPersistDirectory(PersistDirectoryEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setRemovePathProperties(RemovePathPropertiesEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setRemoveSyncPoint(RemoveSyncPointEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setRemoveTable(Table.RemoveTableEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setRename(RenameEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setSetAcl(SetAclEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setSetAttribute(SetAttributeEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setUpdateDatabaseInfo(Table.UpdateDatabaseInfoEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setUpdateUfsMode(UpdateUfsModeEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setUpdateInode(UpdateInodeEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setUpdateInodeDirectory(UpdateInodeDirectoryEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setUpdateInodeFile(UpdateInodeFileEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setAddTransformJobInfo(Table.AddTransformJobInfoEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setRemoveTransformJobInfo(Table.RemoveTransformJobInfoEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setCompleteTransformTable(Table.CompleteTransformTableEntry.getDefaultInstance()).build(),
      JournalEntry.newBuilder().setLoadJob(alluxio.dora.proto.journal.Job.LoadJobEntry.newBuilder()
          .setLoadPath("/test").setState(alluxio.dora.proto.journal.Job.PJobState.CREATED)
          .setBandwidth(1).setPartialListing(false).setVerify(true).setJobId("1").build()).build()
  );
  // CHECKSTYLE.OFF: LineLengthExceed

  @Test
  public void testUnknown() {
    mThrown.expect(IllegalStateException.class);
    JournalEntryAssociation.getMasterForEntry(JournalEntry.getDefaultInstance());
  }

  @Test
  public void testEntries() {
    for (JournalEntry entry : ENTRIES) {
      assertNotNull(JournalEntryAssociation.getMasterForEntry(entry));
    }
  }

  @Test
  public void testFullCoverage() {
    int expectedNumFields = JournalEntry.getDescriptor().getFields().size();
    // subtract 1 for sequence_number
    expectedNumFields--;
    // subtract 1 for operationId
    expectedNumFields--;
    // subtract 1 for journal_entries
    expectedNumFields--;
    assertEquals(expectedNumFields, ENTRIES.size());
  }
}