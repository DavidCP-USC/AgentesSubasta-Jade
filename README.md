# Sistema Multiagentes en Jade

Práctica realizada para la asignatura Computación Distribuida del Grado en Ingeniería Informática de la USC.

# Problema a solucionar

Se busca diseñar un sistema multiagente que permita la compra/venta de libros mediante el procedimiento de subasta al alza (english auction). En este caso se entiende que hay un vendedor que posee uno o más libros a la venta y múltiples compradores interesados  en alguno de sus libros. Los compradores deben de implementar un valor máximo por el cual están dispuestos a pujar y el vendedor el paso o incremento entre dos pujas sucesivas. 

Con el fin de poder apreciar mejor el funcionamiento, se asume que entre dos pujas  sucesivas debe transcurrir un tiempo de 10 segundos, por lo que el vendedor deberá esperar ese tiempo antes de asignar un nuevo precio al libro. La subasta concluye cuando en una ronda todos los posibles compradores indican que no están interesados en el libro, asignándose el mismo al primer comprador que haya pujado por el mismo en la ronda anterior, o bien cuando en la roda actual exista un único comprador interesado.
