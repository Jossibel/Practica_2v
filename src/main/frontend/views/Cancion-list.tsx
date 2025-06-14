import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, ConfirmDialog, Dialog, Grid, GridColumn, GridItemModel, GridSortColumn, HorizontalLayout, Icon, Notification, NumberField, Select, TextField, VerticalLayout } 
from '@vaadin/react-components';
import { CancionService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Cancion',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 1,
    title: 'Cancion',
  },
};

type Cancion = {
  id: string;
  nombre: string;
  genero: string;
  id_genero: string;
  duracion: string;
  tipo: string;
  url: string;
  album: string;
  id_album: string;
};

// Componente para el formulario de nueva canción
function CancionEntryForm({ onCancionCreated }: { onCancionCreated?: () => void }) {
  const nombre = useSignal('');
  const genero = useSignal('');
  const album = useSignal('');
  const duracion = useSignal('');
  const url = useSignal('');
  const tipo = useSignal('');
  
  const [listaGenero, setListaGenero] = useState<Array<{ value: string; label: string }>>([]);
  const [listaAlbum, setListaAlbum] = useState<Array<{ value: string; label: string }>>([]);
  const [listaTipo, setListaTipo] = useState<string[]>([]);
  const dialogOpened = useSignal(false);

  useEffect(() => {
    CancionService.listaAlbumGenero().then(data => {
      setListaGenero(data.map(item => ({ value: item.value, label: item.label })));
    });
    
    CancionService.listAlbumCombo().then(data => {
      setListaAlbum(data.map(item => ({ value: item.value, label: item.label })));
    });
    
    CancionService.listTipo().then(data => {
      setListaTipo(data);
    });
  }, []);

  const createCancion = async () => {
    try {
      if (nombre.value.trim().length > 0 && genero.value.trim().length > 0 && 
          album.value.trim().length > 0 && duracion.value.trim().length > 0 && 
          url.value.trim().length > 0 && tipo.value.trim().length > 0) {
        
        await CancionService.createCancion(
          nombre.value, 
          parseInt(genero.value), 
          parseInt(duracion.value), 
          url.value, 
          tipo.value, 
          parseInt(album.value)
        );
        
        if (onCancionCreated) {
          onCancionCreated();
        }
        
        // Reset form
        nombre.value = '';
        genero.value = '';
        album.value = '';
        duracion.value = '';
        url.value = '';
        tipo.value = '';
        dialogOpened.value = false;
        
        Notification.show('Canción creada', { 
          duration: 3000, 
          position: 'bottom-end', 
          theme: 'success' 
        });
      } else {
        Notification.show('Complete todos los campos requeridos', { 
          duration: 3000, 
          position: 'top-center', 
          theme: 'error' 
        });
      }
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        headerTitle="Nueva canción"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button onClick={() => { dialogOpened.value = false; }}>
              Cancelar
            </Button>
            <Button onClick={createCancion} theme="primary">
              Registrar
            </Button>
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField 
            label="Nombre de la canción"
            placeholder="Ingrese el nombre de la canción"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
          
          <ComboBox 
            label="Género"
            items={listaGenero}
            itemLabelPath="label"
            itemValuePath="value"
            value={genero.value}
            onValueChanged={(evt) => (genero.value = evt.detail.value)}
          />
          
          <ComboBox 
            label="Álbum"
            items={listaAlbum}
            itemLabelPath="label"
            itemValuePath="value"
            value={album.value}
            onValueChanged={(evt) => (album.value = evt.detail.value)}
          />
          
          <ComboBox 
            label="Tipo de archivo"
            items={listaTipo}
            value={tipo.value}
            onValueChanged={(evt) => (tipo.value = evt.detail.value)}
          />
          
          <NumberField 
            label="Duración (segundos)"
            value={duracion.value}
            onValueChanged={(evt) => (duracion.value = evt.detail.value)}
          />
          
          <TextField 
            label="URL de la canción"
            placeholder="Ingrese la URL de la canción"
            value={url.value}
            onValueChanged={(evt) => (url.value = evt.detail.value)}
          />
        </VerticalLayout>
      </Dialog>
      
      <Button onClick={() => { dialogOpened.value = true; }} theme="primary">
        Agregar canción
      </Button>
    </>
  );
}

// Componente para acciones (Editar/Eliminar)
function AccionesRenderer({ item, onEdit, onDelete }: { 
  item: Cancion, 
  onEdit: (item: Cancion) => void, 
  onDelete: (id: string) => void 
}) {
  return (
    <HorizontalLayout theme="spacing">
      <Button theme="primary" onClick={() => onEdit(item)}>
        Editar
      </Button>
      <Button theme="error" onClick={() => onDelete(item.id)}>
        Eliminar
      </Button>
    </HorizontalLayout>
  );
}

// Componente para el formulario de edición
function EditCancionForm({ 
  item, 
  onClose, 
  onSave 
}: { 
  item: Cancion | null, 
  onClose: () => void, 
  onSave: (cancion: Cancion) => void 
}) {
  const nombre = useSignal(item?.nombre || '');
  const genero = useSignal(item?.id_genero || '');
  const album = useSignal(item?.id_album || '');
  const duracion = useSignal(item?.duracion || '');
  const url = useSignal(item?.url || '');
  const tipo = useSignal(item?.tipo || '');

  const [listaGenero, setListaGenero] = useState<Array<{ value: string; label: string }>>([]);
  const [listaAlbum, setListaAlbum] = useState<Array<{ value: string; label: string }>>([]);
  const [listaTipo, setListaTipo] = useState<string[]>([]);

  useEffect(() => {
    if (item) {
      nombre.value = item.nombre;
      genero.value = item.id_genero;
      album.value = item.id_album;
      duracion.value = item.duracion;
      url.value = item.url;
      tipo.value = item.tipo;
    }

    CancionService.listaAlbumGenero().then(data => {
      setListaGenero(data.map(item => ({ value: item.value, label: item.label })));
    });
    
    CancionService.listAlbumCombo().then(data => {
      setListaAlbum(data.map(item => ({ value: item.value, label: item.label })));
    });
    
    CancionService.listTipo().then(data => {
      setListaTipo(data);
    });
  }, [item]);

  const handleSave = () => {
    if (item) {
      const updatedCancion = {
        ...item,
        nombre: nombre.value,
        id_genero: genero.value,
        id_album: album.value,
        duracion: duracion.value,
        url: url.value,
        tipo: tipo.value
      };
      onSave(updatedCancion);
    }
  };

  return (
    <Dialog
      headerTitle="Editar Canción"
      opened={item !== null}
      onOpenedChanged={({ detail }) => {
        if (!detail.value) onClose();
      }}
      footer={
        <>
          <Button onClick={onClose}>Cancelar</Button>
          <Button theme="primary" onClick={handleSave}>
            Guardar Cambios
          </Button>
        </>
      }
    >
      <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
        <TextField 
          label="Nombre de la canción"
          value={nombre.value}
          onValueChanged={(evt) => (nombre.value = evt.detail.value)}
        />
        
        <ComboBox 
          label="Género"
          items={listaGenero}
          itemLabelPath="label"
          itemValuePath="value"
          value={genero.value}
          onValueChanged={(evt) => (genero.value = evt.detail.value)}
        />
        
        <ComboBox 
          label="Álbum"
          items={listaAlbum}
          itemLabelPath="label"
          itemValuePath="value"
          value={album.value}
          onValueChanged={(evt) => (album.value = evt.detail.value)}
        />
        
        <ComboBox 
          label="Tipo de archivo"
          items={listaTipo}
          value={tipo.value}
          onValueChanged={(evt) => (tipo.value = evt.detail.value)}
        />
        
        <NumberField 
          label="Duración (segundos)"
          value={duracion.value}
          onValueChanged={(evt) => (duracion.value = evt.detail.value)}
        />
        
        <TextField 
          label="URL de la canción"
          value={url.value}
          onValueChanged={(evt) => (url.value = evt.detail.value)}
        />
      </VerticalLayout>
    </Dialog>
  );
}

function IndexRenderer({ model }: { model: GridItemModel<Cancion> }) {
  return <span>{model.index + 1}</span>;
}

export default function CancionView() {
  const [items, setItems] = useState<Cancion[]>([]);
  const [editingItem, setEditingItem] = useState<Cancion | null>(null);
  const [deletingId, setDeletingId] = useState<string | null>(null);
  const criterio = useSignal('');
  const texto = useSignal('');
  
  const itemSelect = [
    { label: 'Género', value: 'genero' },
    { label: 'Álbum', value: 'album' },
    { label: 'Nombre', value: 'nombre' }
  ];

  const loadData = async () => {
    try {
      const data = await CancionService.listCancion();
      setItems(data || []);
    } catch (error) {
      handleError(error);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleOrder = async (event: any, columnId: string) => {
    try {
      const direction = event.detail.value;
      const dir = direction === 'asc' ? 1 : 2;
      const data = await CancionService.order(columnId, dir);
      setItems(data || []);
    } catch (error) {
      handleError(error);
    }
  };

  const handleSearch = async () => {
    try {
      if (criterio.value && texto.value) {
        const data = await CancionService.search(criterio.value, texto.value, 0);
        setItems(data || []);
        Notification.show('Búsqueda realizada', { 
          duration: 3000, 
          position: 'bottom-end', 
          theme: 'success' 
        });
      }
    } catch (error) {
      handleError(error);
    }
  };

  const handleEdit = (item: Cancion) => {
    setEditingItem(item);
  };

  const handleDelete = (id: string) => {
    setDeletingId(id);
  };

  const confirmDelete = async () => {
    if (deletingId) {
      try {
        await CancionService.deleteCancion(parseInt(deletingId));
        Notification.show('Canción eliminada', { 
          duration: 3000, 
          position: 'bottom-end', 
          theme: 'success' 
        });
        loadData();
      } catch (error) {
        handleError(error);
      } finally {
        setDeletingId(null);
      }
    }
  };

  const handleSave = async (updatedCancion: Cancion) => {
    try {
      await CancionService.updateCancion(
        parseInt(updatedCancion.id),
        updatedCancion.nombre,
        parseInt(updatedCancion.id_genero),
        parseInt(updatedCancion.duracion),
        updatedCancion.url,
        updatedCancion.tipo,
        parseInt(updatedCancion.id_album)
      );
      
      Notification.show('Canción actualizada', { 
        duration: 3000, 
        position: 'bottom-end', 
        theme: 'success' 
      });
      
      loadData();
      setEditingItem(null);
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Canciones">
        <Group>
          <CancionEntryForm onCancionCreated={loadData} />
        </Group>
      </ViewToolbar>
      
      <HorizontalLayout theme="spacing">
        <Select 
          items={itemSelect}
          value={criterio.value}
          onValueChanged={(evt) => (criterio.value = evt.detail.value)}
          placeholder="Seleccione un criterio"
        />
        
        <TextField
          placeholder="Buscar"
          style={{ width: '50%' }}
          value={texto.value}
          onValueChanged={(evt) => (texto.value = evt.detail.value)}
        >
          <Icon slot="prefix" icon="vaadin:search" />
        </TextField>
        
        <Button onClick={handleSearch} theme="primary">
          Buscar
        </Button>
      </HorizontalLayout>
      
      <Grid items={items}>
        <GridColumn header="Nro" renderer={IndexRenderer} />
        <GridSortColumn 
          path="nombre" 
          header="Nombre" 
          onDirectionChanged={(e) => handleOrder(e, "nombre")} 
        />
        <GridSortColumn 
          path="genero" 
          header="Género" 
          onDirectionChanged={(e) => handleOrder(e, "genero")} 
        />
        <GridSortColumn 
          path="album" 
          header="Álbum" 
          onDirectionChanged={(e) => handleOrder(e, "album")} 
        />
        <GridSortColumn 
          path="duracion" 
          header="Duración" 
          onDirectionChanged={(e) => handleOrder(e, "duracion")} 
        />
        <GridSortColumn 
          path="tipo" 
          header="Tipo" 
          onDirectionChanged={(e) => handleOrder(e, "tipo")} 
        />
        <GridColumn 
          header="Acciones" 
          renderer={({ item }) => (
            <AccionesRenderer 
              item={item} 
              onEdit={handleEdit} 
              onDelete={handleDelete} 
            />
          )} 
        />
      </Grid>

      <EditCancionForm 
        item={editingItem} 
        onClose={() => setEditingItem(null)} 
        onSave={handleSave} 
      />

      <ConfirmDialog
        header="Eliminar Canción"
        cancelButtonVisible
        confirmText="Eliminar"
        confirmTheme="error"
        opened={deletingId !== null}
        onCancel={() => setDeletingId(null)}
        onConfirm={confirmDelete}
      >
        ¿Estás seguro de que deseas eliminar esta canción?
      </ConfirmDialog>
    </main>
  );
}
   