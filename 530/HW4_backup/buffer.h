/*
 *  buffer.h
 *  HW4_working
 *
 *  Created by duozhao on 10/11/11.
 *  Copyright 2011 UNC Chapel Hill. All rights reserved.
 *
 */

typedef struct{
	char *bufferContent;
	
	int head; //beginning of the data where the consumer starts
	int tail; //End of the data where the producer deposits.
	
	int status; // (-1)(flushed) 0(Empty), 1(Ordinary), 2(Full) Full or empty -1
	
	int size; //size == vacant + occupied
	int occupied;
	int vacant;
	
	semaphore occupiedLock; //conting semaphore
	semaphore vacantLock;
	
	semaphore mutex; 
	
	st_mutex_t mtx; //binary semaphore
	
}circularBuffer;

extern int newBuffer(circularBuffer *buf, int suggestedSize);
extern int flushBuffer(circularBuffer *buf);
extern bool isConsistent(circularBuffer *buf);
extern bool isFull(circularBuffer *buf);
extern bool isEmpty(circularBuffer *buf);
extern int depositCirBuf(circularBuffer *buf, char source);
extern int withdrawalCirBuf(char *destination, circularBuffer *buf);
extern int getSize(circularBuffer *buf);
extern int getVacantSlots(circularBuffer *buf);
extern int getOccupiedSlots(circularBuffer *buf);
extern int getHeadIndex(circularBuffer *buf);
extern int getTailIndex(circularBuffer *buf); 