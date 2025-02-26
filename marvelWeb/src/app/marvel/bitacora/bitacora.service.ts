import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { HttpClient, HttpHeaders, HttpErrorResponse} from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class BitacoraService {

  private urlBitacora: string = 'http://localhost:9001/consumer/api/marvel/getDataBitacora';

  constructor(private http: HttpClient, private router: Router) { }


  async getDatosBitacora(): Promise<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json; charset=utf-8' });

    try {

      const response$ = this.http.get(this.urlBitacora, { headers: headers }).pipe(
        catchError((error: HttpErrorResponse) => {

          let mensajeError: string = 'Error inesperado';

          if (error.status === 403) {

            mensajeError = 'Tu sesión ha expirado, por favor inicia sesión nuevamente';

          }

          throw new Error(mensajeError);

        })

      );

      const response = await firstValueFrom(response$);
      return response;

    } catch (error) {

      Swal.fire({
        title: `<h5>${ (error as Error).message}</h5>`,
        icon : 'error',
        confirmButtonText: 'Aceptar',
        customClass: {
          confirmButton: 'btn btn-sm btn-outline-primary',
        },
        buttonsStyling: false
      }).then(() => {

        this.router.navigate(['/login']);

      });

    }
  }

}
