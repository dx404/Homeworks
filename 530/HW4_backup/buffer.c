/*
 *  buffer.c
 *  HW4_working
 *
 *  Created by duozhao on 10/11/11.
 *  Copyright 2011 UNC Chapel Hill. All rights reserved.
 *
 */

#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdbool.h>

#include "st.h"
#include "semaphore.h"
#include "buffer.h"

int newBuffer(circularBuffer *buf, int suggestedSize){
	buf->status = 0; //0 for empty;
	//circularBuffer initializer, Construct an empty circularBuffer with a given size.  
	buf->bufferContent = (char*) malloc ((suggestedSize+1) * sizeof(char)); //reserved one more for null pointer.
	
	buf->head = 0;
	buf->tail = 0; // tail pointer to the next immediate avaiable slot. 
	
	buf->size = suggestedSize; //virtual/logic size. 
	buf->vacant = suggestedSize;
	buf->occupied = 0;
	
	createSem(&(buf->vacantLock), suggestedSize);
	createSem(&(buf->occupiedLock), 0);
	createSem(&(buf->mutex), 1);	
	buf->mtx = st_mutex_new();
	return 0;
}

int flushBuffer(circularBuffer *buf){ //flush to a single slot buffer. 
	buf->status = -1; // Flushed.
	buf->bufferContent = NULL; 
	
	buf->head = 0;
	buf->tail = 0; // tail pointer to the next immediate avaiable slot. 
	
	buf->size = 0; //virtual/logic size. 
	buf->vacant = 0;
	buf->occupied = 0;
	
	return 0;
}

bool isFull(circularBuffer *buf){
	if (buf->status == 2) {
		return true;
	}
	return false;
}

bool isEmpty(circularBuffer *buf){
	if (buf->status == 0) {
		return true;
	}
	return false;
}

int depositCirBuf(circularBuffer *buf, char source){ //only one character.
	
	down(&(buf->vacantLock));
	st_mutex_lock(buf->mtx);
	
	buf->bufferContent[buf->tail] = source; //hard copy
	buf->tail = (buf->tail+1)%(buf->size);
	buf->occupied++;
	buf->vacant--;
	if (buf->vacant == 0) {
		buf->status = 2;
	}
	else {
		buf->status = 1; //Neither full nor empty
	}
	
	st_mutex_unlock(buf->mtx);
	up(&(buf->occupiedLock));
	
	return 1;
}

int withdrawalCirBuf(char *destination, circularBuffer *buf){
	
	
	down(&(buf->occupiedLock));
	st_mutex_lock(buf->mtx);
	
	*destination = buf->bufferContent[buf->head];
	buf->head = (buf->head+1)%(buf->size);
	buf->vacant++;
	buf->occupied--;
	if (buf->occupied == 0) {
		buf->status = 0;
		st_mutex_unlock(buf->mtx);
		
	}else {
		buf->status = 1; //Neither full nor empty
	}

	st_mutex_unlock(buf->mtx);
	up(&(buf->vacantLock));
	
	return 1;
}

int getSize(circularBuffer *buf){
	return buf->size;
}

int getVacantSlots(circularBuffer *buf){
	return buf->vacant;
}

int getOccupiedSlots(circularBuffer *buf){
	return buf->occupied;
}

int getHeadIndex(circularBuffer *buf){
	return buf->head;
}

int getTailIndex(circularBuffer *buf){
	return buf->tail;
}

bool isConsistent(circularBuffer *buf){
	if (buf->status==-1) {
		printf("Flushed Status, bufferContent Uninitialized \n");
		return false;
	}
	if (buf->bufferContent == NULL) {
		printf("bufferContent Uninitialized, No Buffer Boundary Specified \n");
		return false;
	}
	if (buf->head < 0 || buf->head >= buf->size) {
		printf("Head Out of Buffer\n");
		return false;
	}
	if (buf->tail < 0 || buf->tail >= buf->size) {
		printf("Tail Out of Buffer\n");
		return false;
	}
	if (buf->vacant < 0 || buf->head > buf->size) {
		printf("Vacant Seats Out of Size\n");
		return false;
	}
	if (buf->occupied < 0 || buf->occupied > buf->size) {
		printf("Occupied Seats Out of Size\n");
		return false;
	}
	if (buf->size != buf->vacant + buf->occupied) {
		printf("BufferSize is not equal to Vacant + Occupied \n");
		return false;
	}
	if (buf->tail > buf->head) {
		if (buf->tail - buf->head != buf->occupied) {
			printf("Tail - Head != Occupied\n");
			return false;
		}
	}
	if (buf->tail < buf->head) {
		if (buf->head - buf->tail != buf->vacant) {
			printf("Wrapped Array Inconsistency!\n");
			return false;
		}
	}
	if (buf->tail == buf->head && buf->vacant!=0 && buf->occupied!=0) {
		printf("Tail and Head meet in inconsistency\n");
		return false;
	}
	return true;
}
