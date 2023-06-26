/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3clasesjava;

/**
 *
 * @author Han-S
 */
public class Pila {

    final int MAX = 5000;
    SimbGram[] _elems;
    int _tope;

    public Pila() {
        _elems = new SimbGram[MAX];
        for (int i = 0; i < _elems.length; i++) {
            _elems[i] = new SimbGram("");
        }
        _tope = 0;
    }

    public boolean Empty() {
        return _tope == 0 ? true : false;
    }

    public boolean Full() {
        return _tope == _elems.length ? true : false;
    }

    public void Push(SimbGram oElem) {
        _elems[_tope++] = oElem;
    }

    public int Length() {
        return _tope;
    }

    public SimbGram Pop() {
        return _elems[--_tope];
    }

    public void Inicia() {
        _tope = 0;
    }

    public SimbGram Tope() {
        return _elems[_tope - 1];
    }
}
