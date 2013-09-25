/*
 *  semaphore.c
 *  HW4_working
 *
 *  Created by duozhao on 10/15/11.
 *  Copyright 2011 UNC Chapel Hill. All rights reserved.
 *
 */
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>

#include "st.h"
#include "semaphore.h"

void down(semaphore *s){
	if (s->value==0) {
		st_cond_wait(s->sem_queue);
	} 
	assert(s->value>0);
	(s->value)--; 
}
// semaphore value ==0, to be blocked, otherwise
// otherwise, decreament the semaphore value

void up(semaphore *s){
	(s->value)++;
	assert(s->value>0);
	st_cond_signal(s->sem_queue);
}
// increase the semaphore value
// signal other candiate process to resume

void createSem(semaphore *s, int value){
	s->value = value;
	assert(s->value>=0);
	s->sem_queue = st_cond_new();
}
// the semaphore initializer 
