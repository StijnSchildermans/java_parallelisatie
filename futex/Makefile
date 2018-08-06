# Makefile for dedup kernel

TARGET=futex

CFLAGS += -Wall -fno-strict-aliasing -D_XOPEN_SOURCE=600 -pthread

LIBS += -lm

FUTEX_OBJ = Futex.o

CFLAGS += -DENABLE_PTHREADS
LIBS += -lz

all: $(TARGET)

.c.o:
	$(CC) -c $(CFLAGS) $< -o $@

$(TARGET): $(FUTEX_OBJ)
	$(CC) $(CFLAGS) $(LDFLAGS) -o $(TARGET) $(FUTEX_OBJ) $(LIBS)

clean:
	rm -f *~ *.o $(TARGET)
