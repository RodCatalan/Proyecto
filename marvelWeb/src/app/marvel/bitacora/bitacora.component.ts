import { Component, OnInit } from '@angular/core';
import { BitacoraService } from './bitacora.service';
import Swal from 'sweetalert2';

@Component({
  templateUrl: './bitacora.component.html',
  styleUrls: ['./bitacora.component.scss']
})
export class BitacoraComponent implements OnInit {

  public registrosBitacora: Array<{ idBitacora: number; path: string; horaConsulta: string; }> = [];

  constructor(private bitacoraService : BitacoraService) { }

  ngOnInit(): void {

    this.getDatosBitacora();

  }

  async getDatosBitacora(){

    const response = await this.bitacoraService.getDatosBitacora();

    if (response && response.length > 0) {

      this.registrosBitacora = response;

    }

  }

}
