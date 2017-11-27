from Tkinter import *
from math import sqrt
import pdb

def drawPoint(canvas, x, y, color="black"):
    canvas.create_rectangle(x, y, x, y, fill=color, width=0)

def drawLine(canvas, sourceX, sourceY, goalX, goalY):
    drawPoint(canvas, sourceX, sourceY)
    if abs(sourceX - goalX) > 1 and abs(sourceY - goalY) > 1:
        drawLine(canvas, sourceX + goalX /100, sourceY + goalY / 100, goalX, goalY)

def drawTriangle(canvas, aX, aY, bX, bY, cX, cY):
    canvas.create_line( aX, aY, bX, bY)
    canvas.create_line( aX, aY, cX, cY)
    canvas.create_line( bX, bY, cX, cY)
    if abs(aX - bX) > 1:

        newAx = (aX + bX) / 2 
        newAy = (aY + bY) / 2 

        newBx = (bX + cX) / 2 
        newBy = (bY + cY) / 2 

        newCx = (cX + aX) / 2 
        newCy = (cY + aY) / 2 
        drawTriangle2(canvas, newAx, newAy, newBx, newBy, newCx, newCy)

def drawTriangle2(canvas, aX, aY, bX, bY, cX, cY):
    canvas.create_line( aX, aY, bX, bY)
    canvas.create_line( aX, aY, cX, cY)
    canvas.create_line( bX, bY, cX, cY)
    if abs(aX - bX) > 1:

        newAx = (aX + bX) / 2 
        newAy = (aY + bY) / 2 

        newBx = (bX + cX) / 2 
        newBy = (bY + cY) / 2 

        newCx = (cX + aX) / 2 
        newCy = (cY + aY) / 2 
        drawTriangle2(canvas, newAx, newAy, newBx, newBy, newCx, newCy)

def abs(n):
    return sqrt(n*n)

if __name__ == "__main__":
    window = Tk()
    w = Canvas(window, width=800, height=400)
    w.pack()
    #w.create_line(0, 0, 100, 100)
    #w.create_line(0, 100, 200, 0, fill="red")
    #w.create_rectangle(50, 25, 150, 75, fill="blue")
    drawPoint(w,10 ,10)
    #drawLine(w, 0,0,100,100)
    drawTriangle(w, 0 ,0, 800,0, 400, 400)
    mainloop()
#usage

