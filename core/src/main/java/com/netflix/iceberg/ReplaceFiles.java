/*
 * Copyright 2017 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.iceberg;

import com.google.common.base.Preconditions;
import java.util.Set;

class ReplaceFiles extends MergingSnapshotUpdate implements RewriteFiles {
  ReplaceFiles(TableOperations ops) {
    super(ops);

    // replace files must fail if any of the deleted paths is missing and cannot be deleted
    failMissingDeletePaths();
  }

  @Override
  public RewriteFiles rewriteFiles(Set<DataFile> filesToDelete, Set<DataFile> filesToAdd) {
    Preconditions.checkArgument(filesToDelete != null && !filesToDelete.isEmpty(),
        "Files to delete cannot be null or empty");
    Preconditions.checkArgument(filesToAdd != null && !filesToAdd.isEmpty(),
        "Files to add can not be null or empty");

    for (DataFile toDelete : filesToDelete) {
      delete(toDelete.path());
    }

    for (DataFile toAdd : filesToAdd) {
      add(toAdd);
    }

    return this;
  }
}
