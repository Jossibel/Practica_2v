import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import {
  Button, ComboBox, Dialog, Grid, GridColumn, GridItemModel,
  NumberField, TextField, VerticalLayout
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { CancionServices } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import Cancion from 'Frontend/generated/com/unl/practica2/base/models/Cancion';
import { useEffect } from 'react';

export const config: ViewConfig = {
  title: 'Canción',
  menu: {
    icon: 'vaadin:music',
    order: 1,
    title: 'Canción',
  },
};

type CancionEntryFormProps = {
  onCancionCreated?: () => void;
};



// FORMULARIO NUEVA CANCIÓN
function CancionEntryForm(props: CancionEntryFormProps) {
  const nombre = useSignal('');
  const genero = useSignal('');
  const album = useSignal('');
  const duracion = useSignal('');
  const url = useSignal('');
  const tipo = useSignal('');
  const dialogOpened = useSignal(false);

  const createCancion = async () => {
    try {
      if (
        nombre.value.trim() && genero.value.trim() &&
        album.value.trim() && duracion.value.trim() &&
        url.value.trim() && tipo.value.trim()
      ) {
        const id_genero = parseInt(genero.value) + 1;
        const id_album = parseInt(album.value) + 1;

        await CancionServices.create(
          nombre.value,
          id_genero,
          parseInt(duracion.value),
          url.value,
          tipo.value,
          id_album
        );

        props.onCancionCreated?.();

        nombre.value = '';
        genero.value = '';
        album.value = '';
        duracion.value = '';
        url.value = '';
        tipo.value = '';
        dialogOpened.value = false;

        Notification.show('Cancion creada', {
          duration: 5000,
          position: 'bottom-end',
          theme: 'success',
        });
      } else {
        Notification.show('No se pudo crear, faltan datos', {
          duration: 5000,
          position: 'top-center',
          theme: 'error',
        });
      }
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  const listaGenero = useSignal<String[]>([]);
  const listaAlbum = useSignal<String[]>([]);
  const listaTipo = useSignal<String[]>([]);

  useEffect(() => {
    CancionServices.listaAlbumGenero().then(data => listaGenero.value = data);
    CancionServices.listaAlbumCombo().then(data => listaAlbum.value = data);
    CancionServices.listTipo().then(data => listaTipo.value = data);
  }, []);

  return (
    <>
      <Dialog
        modeless
        headerTitle="Nueva cancion"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button onClick={() => dialogOpened.value = false}>Cancelar</Button>
            <Button onClick={createCancion} theme="primary">Registrar</Button>
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre de la cancion"
            placeholder="Ingrese el nombre"
            value={nombre.value}
            onValueChanged={(evt) => nombre.value = evt.detail.value}
          />
          <ComboBox label="Genero"
            items={listaGenero.value}
            placeholder="Seleccione un genero"
            value={genero.value}
            onValueChanged={(evt) => genero.value = evt.detail.value}
          />
          <ComboBox label="Album"
            items={listaAlbum.value}
            placeholder="Seleccione un album"
            value={album.value}
            onValueChanged={(evt) => album.value = evt.detail.value}
          />
          <ComboBox label="Tipo de archivo"
            items={listaTipo.value}
            placeholder="Seleccione tipo"
            value={tipo.value}
            onValueChanged={(evt) => tipo.value = evt.detail.value}
          />
          <NumberField label="duracion (segundos)"
            placeholder=" 180"
            value={duracion.value}
            onValueChanged={(evt) => duracion.value = evt.detail.value}
          />
          <TextField label="Url"
            placeholder="Ingrese el link de la cancion"
            value={url.value}
            onValueChanged={(evt) => url.value = evt.detail.value}
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={() => dialogOpened.value = true}>Agregar Canción</Button>
    </>
  );
}



// LISTA DE CANCIONES
export default function CancionView() {
  const dataProvider = useDataProvider<Cancion>({
    list: () => CancionServices.listCancion(),
  });

  function indexIndex({ model }: { model: GridItemModel<Cancion> }) {
    return <span>{model.index + 1}</span>;
  }



  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Lista de canciones">
        <Group>
          <CancionEntryForm onCancionCreated={dataProvider.refresh} />
        </Group>
      </ViewToolbar>
      <Grid dataProvider={dataProvider.dataProvider}>
        <GridColumn renderer={indexIndex} header="Nro" />
        <GridColumn path="nombre" header="Cancion" />
        <GridColumn path="genero" header="Genero" />
        <GridColumn path="album" header="Album" />
        <GridColumn path="tipo" header="Tipo" />
        <GridColumn path="duracion" header="duracion (s)" />
        <GridColumn path="url" header="Ruta" />


      </Grid>
    </main>
  );
}