/*
 * Copyright 2015 Naver Corp.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.nbasearc.confmaster.server.cluster;

import static org.junit.Assert.*;

import org.junit.Test;

import com.navercorp.nbasearc.confmaster.repository.znode.PartitionGroupData;

public class PartitionGroupTest {

    @Test
    public void cleanMGen() {
        final int mgenHistorySize = 5;
        
        PartitionGroupData d = new PartitionGroupData();
        d = PartitionGroupData.builder().from(d).addMasterGen(0L)
                .addMasterGen(100L).addMasterGen(200L).build();
        assertEquals(2, d.currentGen());
        assertEquals(3, d.getMasterGenMap().size());

        // Clean
        d = PartitionGroupData.builder().from(d)
                .cleanMGen(mgenHistorySize).build();
        assertEquals(2, d.currentGen());
        assertEquals(Long.valueOf(200L), d.currentSeq());
        assertEquals(3, d.getMasterGenMap().size());

        // Add masterGen
        d = PartitionGroupData.builder().from(d).addMasterGen(300L).build();
        assertEquals(3, d.currentGen());
        assertEquals(Long.valueOf(300L), d.currentSeq());
        assertEquals(4, d.getMasterGenMap().size());

        d = PartitionGroupData.builder().from(d)
                .cleanMGen(mgenHistorySize).build();
        assertEquals(3, d.currentGen());
        assertEquals(Long.valueOf(300L), d.currentSeq());
        assertEquals(4, d.getMasterGenMap().size());
        
        d = PartitionGroupData.builder().from(d).addMasterGen(400L)
                .addMasterGen(500L).build();
        d = PartitionGroupData.builder().from(d)
                .cleanMGen(mgenHistorySize).build();
        assertEquals(mgenHistorySize, d.getMasterGenMap().size());
        assertEquals(5, d.currentGen());
        assertEquals(100L, mgenHistorySize, d.getMasterGenMap().get(1));
        assertEquals(200L, mgenHistorySize, d.getMasterGenMap().get(2));
        assertEquals(300L, mgenHistorySize, d.getMasterGenMap().get(3));
        assertEquals(400L, mgenHistorySize, d.getMasterGenMap().get(4));
        assertEquals(500L, mgenHistorySize, d.getMasterGenMap().get(5));
    }

}
