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

#ifndef _LOG_MMAP_H_
#define _LOG_MMAP_H_

#include "common.h"

typedef enum
{
  GET_CREATE = 0,
  GET_EXIST = 1
} entryGetOpt;

extern logMmapEntry *logmmap_entry_get (smrReplicator * rep, long long seq,
					entryGetOpt opt, int is_write);
extern void logmmap_entry_release (smrReplicator * rep, logMmapEntry * entry);
extern void logmmap_entry_release_raw (smrReplicator * rep,
				       logMmapEntry * entry);

extern logMmapEntry *logmmap_entry_addref (smrReplicator * rep,
					   logMmapEntry * entry);
#endif
